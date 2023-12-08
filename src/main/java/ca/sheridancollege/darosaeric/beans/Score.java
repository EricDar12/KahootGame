package ca.sheridancollege.darosaeric.beans;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Score {
	
	private Long id;
	private String username;
	private int score;
	private String subject;

}
