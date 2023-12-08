package ca.sheridancollege.darosaeric.beans;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Question {
	
	private Long id;
	private String question_text;
	private String option_a;
	private String option_b;
	private String option_c;
	private String option_d;
	private String correct_option;
	private final String[] OPTIONS = {"A", "B", "C", "D"};
	private String subject;
	private final String[] SUBJECTS = {"Java", "Science"};
	private final Map<String, String> optionsMap = new HashMap<>();
	
	public String getCorrectAnswer() { // Returns the correct answer String
		
		optionsMap.put("A", option_a);
		optionsMap.put("B", option_b);
		optionsMap.put("C", option_c);
		optionsMap.put("D", option_d);
		
		return optionsMap.get(correct_option);

	}
}
