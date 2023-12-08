package ca.sheridancollege.darosaeric.controllers;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sheridancollege.darosaeric.beans.Question;
import ca.sheridancollege.darosaeric.beans.Score;
import ca.sheridancollege.darosaeric.database.DatabaseAccess;

@Controller
public class KahootController {
	
	@Autowired
	@Lazy
	private DatabaseAccess da;
	
	private List<Question> questionList = new ArrayList<>();
	private Question currentQuestion;
	private String selectedSubject;
	private int score = 0;
	private int scoreMultiplier = 1;
	
	@GetMapping("/")
	public String index() {
		return "index";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/register")
	public String register() {
		return "register";
	}
	
	// Registers user information into user DB table
	@PostMapping("/register")
	public String postRegister(@RequestParam String username, @RequestParam String password, Model model) {
		
		if (da.findUserAccount(username) == null) { // If account does not exist, add it
			da.addUser(username, password);
			
			Long userId = da.findUserAccount(username).getUserId();
			da.addRole(userId, Long.valueOf(2)); // Accounts currently hard coded to "GUEST" 
			
			return "redirect:/";
		} 
		else { // If account exists, don't create it and display an error message
			model.addAttribute("error", "Account already Exists");
			return "register";
		}
	}
	
	@GetMapping("/error")
	public String error() {
		return "error/permission-denied";
	}
	
	@GetMapping("/secure")
	public String secure(Model model) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = authentication.getName();
		
	    model.addAttribute("scoreList", da.getScoreList()); // Add all scores to model and display
	    model.addAttribute("user", username);	
		model.addAttribute("question", new Question());
		
		return "secure/index";
	}
	
	// Method for returning an editable list of questions based on the selected subject
	@PostMapping("/secure/quizList")
	public String quizList(Model model, @RequestParam String subject) {
		
		selectedSubject = subject; // Sets global variable to the selected subject
		
		model.addAttribute("questionList", da.getQuestionsBySubject(selectedSubject));
		model.addAttribute("question", new Question());
		
		return "secure/quizList";
	}
	
	// Adds a new question into the database
	@PostMapping("/secure/addQuestion")
	public String addQuestion(Model model, @ModelAttribute Question question) {
		
		List<Question> existingQuestion = da.getQuestionListById(question.getId());
		
		if (existingQuestion.isEmpty()) { // If question does not exist, add it
			da.addQuestion(question);
		} else { // If question exists, edit it
			da.editQuestionById(question);
		}
				
		model.addAttribute("questionList",da.getQuestionsBySubject(selectedSubject));
		model.addAttribute("question", new Question());
		
		return "secure/quizList";
	}
	
	// Methods for deleting questions via Path Variables
	@GetMapping("/secure/deleteQuestionById/{id}")
	public String deleteQuestionById(Model model, @PathVariable Long id) {
		
		da.deleteQuestionById(id);
		model.addAttribute("question", new Question());
		model.addAttribute("questionList", da.getQuestionsBySubject(selectedSubject));
		
		return "secure/quizList";
	}
	
	// Method for editing questions via Path Variables
	@GetMapping("/secure/editQuestionById/{id}")
	public String editQuestionById(Model model, @PathVariable Long id) {
		
		Question question = da.getQuestionListById(id).get(0);
		da.editQuestionById(question);
		
		model.addAttribute("question", question);
		model.addAttribute("questionList", da.getQuestionsBySubject(selectedSubject));
		
		return "secure/quizList";
	}

	// Method for processing the users selected subject and generating a quiz
	@PostMapping("/secure/selectSubject")
	public String getQuestion(Model model, @RequestParam String subject) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = authentication.getName();

		selectedSubject = subject; // Sets global variable to the selected subject
		
		questionList = da.getQuestionsBySubject(selectedSubject); // Storing list of questions for desired subject
		Collections.shuffle(questionList); // Shuffling list of questions
		model.addAttribute("scoreList", da.getScoreListBySubject(selectedSubject));
		model.addAttribute("subject", subject);
		model.addAttribute("user", username);
							
		return "secure/selectSubject";
	}
		
	// Method to load the next question into the model 
	@PostMapping("/secure/nextQuestion")
	public String nextQuestion(Model model) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = authentication.getName();
		
		if (questionList.isEmpty()) { // When quiz is complete, process the score and check if new high score
			
			// If a score for this user and subject does not exist, add it
			if (da.getScoreByUsernameAndSubject(username, selectedSubject).isEmpty()) {
				
				Score highscore = new Score();
				highscore.setUsername(username);
				highscore.setScore(score);
				highscore.setSubject(selectedSubject);
				model.addAttribute("message", "Nice job! Score added to database");
				
				da.addNewHighScore(highscore); // Add score to DB 
			}
			// If current score is greater than the users existing score in DB, replace it
			else if (score > da.getScoreByUsernameAndSubject(username, selectedSubject).get(0).getScore()) {
				
				Score highscore = new Score();
				highscore.setUsername(username);
				highscore.setScore(score);
				highscore.setSubject(selectedSubject);
				
				da.updateHighScore(highscore);
				model.addAttribute("message", "Congratz! New Highscore!");
			}
			
			model.addAttribute("user", username);
			model.addAttribute("score", score);
			
			score = 0; // Reset score and multiplier after game is complete
			scoreMultiplier = 1;
			return "secure/score";
		}
		else { // If questions remain in the quiz, add the next one to the model
			
		model.addAttribute("user", username);
		model.addAttribute("score", score);
		
		currentQuestion = questionList.get(0);
		model.addAttribute("question", currentQuestion);
		
		return "secure/nextQuestion";
		}
	}
	
	// Method for evaluating a users answer to a specific question
		@PostMapping("/secure/processAnswer")
		public String processAnswer(Model model, @RequestParam String userAnswer) {
			
			model.addAttribute("question", currentQuestion);
		
			if (userAnswer.equals(currentQuestion.getCorrect_option())) {
				int points = 10 * scoreMultiplier; // Multiply points by amount of correct answers in a row
				score += points; 
				scoreMultiplier++; // Increment multiplier with each correct answer
				
				model.addAttribute("evaluation", "You are Correct!");
				model.addAttribute("points", points);
				model.addAttribute("correctAnswer", currentQuestion.getCorrectAnswer());
				
			} else {
				model.addAttribute("evaluation", "You are Incorrect! Better luck next time");
				model.addAttribute("correctAnswer", currentQuestion.getCorrectAnswer());
				model.addAttribute("points", 0);
				scoreMultiplier = 1;
			}
			
			questionList.remove(0); // Remove question from list after it is evaluated
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		    String username = authentication.getName();
		    
			model.addAttribute("user", username);
			model.addAttribute("score", score);
			
			return "secure/processAnswer";
		}
		
		@PostMapping("/secure/exitQuiz")
		public String exitQuiz(Model model) {
			
			// Reset questionList, score and multiplier on exit
			questionList = new ArrayList<>();
			score = 0; 
			scoreMultiplier = 1;
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		    String username = authentication.getName();
			
			model.addAttribute("user", username);	
			model.addAttribute("scoreList", da.getScoreList());
			model.addAttribute("question", new Question());
			
			return "secure/index";
		}
}
