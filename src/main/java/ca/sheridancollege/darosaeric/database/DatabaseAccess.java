package ca.sheridancollege.darosaeric.database;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.darosaeric.beans.Question;
import ca.sheridancollege.darosaeric.beans.Score;
import ca.sheridancollege.darosaeric.beans.User;

@Repository
public class DatabaseAccess {

	@Autowired
	protected NamedParameterJdbcTemplate jdbc;

	@Autowired
	@Lazy
	private BCryptPasswordEncoder passwordEncoder;
	
	// Method to retrieve score based on user name
	public List<Score> getScoreByUsernameAndSubject(String username, String subject) {
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT * FROM scores WHERE username = :username AND subject = :subject";
		
		namedParameters.addValue("username", username);
		namedParameters.addValue("subject", subject);
		
		return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<Score>(Score.class));
	}
	
	// Method to add a new score to the database
	public void addNewHighScore(Score score) {
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "INSERT INTO scores (username, score, subject) VALUES (:username, :score, :subject)";
		
		namedParameters.addValue("username", score.getUsername());
		namedParameters.addValue("score", score.getScore());
		namedParameters.addValue("subject", score.getSubject());
		
		jdbc.update(query, namedParameters);
	}
	
	// Method to update an existing score within the database
	public void updateHighScore(Score score) {
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "UPDATE scores SET score = :score WHERE username = :username AND subject = :subject";
		
		namedParameters.addValue("score", score.getScore());
		namedParameters.addValue("username", score.getUsername());
		namedParameters.addValue("subject", score.getSubject());
		
		int rowsAffected = jdbc.update(query, namedParameters);
		if (rowsAffected > 0) {
			System.out.println("Edit Made to Scores Table");
		}	
	}
	
	// Method to add questions into the question DB table
	public void addQuestion(Question question) {

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "INSERT INTO questions (question_text, option_a, option_b, option_c, option_d, correct_option, subject)"
				+ " VALUES (:question_text, :option_a, :option_b, :option_c, :option_d, :correct_option, :subject)";

		namedParameters.addValue("question_text", question.getQuestion_text());
		namedParameters.addValue("option_a", question.getOption_a());
		namedParameters.addValue("option_b", question.getOption_b());
		namedParameters.addValue("option_c", question.getOption_c());
		namedParameters.addValue("option_d", question.getOption_d());
		namedParameters.addValue("correct_option", question.getCorrect_option());
		namedParameters.addValue("subject", question.getSubject());

		int rowsAffected = jdbc.update(query, namedParameters);
		if (rowsAffected > 0) {
			System.out.println("Added to Question Table");
		}
	}

	// Method to retrieve question list
	public List<Question> getQuestionList() {

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT * FROM questions";

		return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<Question>(Question.class));
	}
	
	// Method to retrieve question by ID
	public List<Question> getQuestionListById(Long id) {
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT * FROM questions WHERE id = :id";
		
		namedParameters.addValue("id", id);
		
		return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<Question>(Question.class));	
	}
	
	// Method to delete question by ID
	public void deleteQuestionById(Long id) {
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "DELETE FROM questions WHERE id = :id";
		
		namedParameters.addValue("id", id);
		
		if (jdbc.update(query, namedParameters) > 0) {
			System.out.println("Deleted Question " + id + " from the database");
		}
	}
	
	public void editQuestionById(Question question) {
		
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "UPDATE questions SET question_text = :question_text, option_a = :option_a, option_b = :option_b, option_c = :option_c, option_d = :option_d, correct_option = :correct_option, subject = :subject WHERE id = :id";

		namedParameters.addValue("question_text", question.getQuestion_text());
		namedParameters.addValue("option_a", question.getOption_a());
		namedParameters.addValue("option_b", question.getOption_b());
		namedParameters.addValue("option_c", question.getOption_c());
		namedParameters.addValue("option_d", question.getOption_d());
		namedParameters.addValue("correct_option", question.getCorrect_option());
		namedParameters.addValue("subject", question.getSubject());
		namedParameters.addValue("id", question.getId());

		int rowsAffected = jdbc.update(query, namedParameters);
		if (rowsAffected > 0) {
			System.out.println("Edit Made to Question Table");
		}
		
	}
	
	// Retrieve list of Questions by subject
	public List<Question> getQuestionsBySubject(String subject) {

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT * FROM questions where subject = :subject";
		namedParameters.addValue("subject", subject);

		return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<Question>(Question.class));
	}

	// Retrieve list of all scores
	public List<Score> getScoreList() {

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT * FROM scores ORDER BY score DESC";

		return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<Score>(Score.class));
	}

	// Retrieve list of scores by subject
	public List<Score> getScoreListBySubject(String subject) {

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT * FROM SCORES WHERE subject = :subject ORDER BY score DESC";
		namedParameters.addValue("subject", subject);

		return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<Score>(Score.class));
	}

	// Finds User Account from Email
	public User findUserAccount(String email) {

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT * FROM sec_user where email = :email";
		namedParameters.addValue("email", email);

		try {
			return jdbc.queryForObject(query, namedParameters, new BeanPropertyRowMapper<>(User.class));

		} catch (EmptyResultDataAccessException erdae) { // If user is not found, return null
			return null;
		}
	}

	// Method to get User Roles for a specific User id
	public List<String> getRolesById(Long userId) {

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT sec_role.roleName " + "FROM user_role, sec_role "
				+ "WHERE user_role.roleId = sec_role.roleId " + "AND userId = :userId";

		namedParameters.addValue("userId", userId);
		return jdbc.queryForList(query, namedParameters, String.class);
	}

	// Method to add a user to the user database
	public void addUser(String email, String password) {

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "INSERT INTO sec_user (email, encryptedPassword, enabled) VALUES (:email, :encryptedPassword, 1)";

		namedParameters.addValue("email", email);
		namedParameters.addValue("encryptedPassword", passwordEncoder.encode(password)); // Encrypting password before
																							// entry

		jdbc.update(query, namedParameters);
	}

	// Method to add specific roles to a user
	public void addRole(Long userId, Long roleId) {

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "INSERT INTO user_role (userId, roleId) VALUES (:userId, :roleId)";

		namedParameters.addValue("userId", userId);
		namedParameters.addValue("roleId", roleId);

		jdbc.update(query, namedParameters);
	}

}
