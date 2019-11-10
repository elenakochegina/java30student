package telran.java30.student.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentResponseDto {
	Integer id;
	String name;
	@Singular Map <String ,Integer> scores;
}
