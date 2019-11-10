package telran.java30.student.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import telran.java30.student.dao.StudentRepository;
import telran.java30.student.dto.ScoreDto;
import telran.java30.student.dto.StudentDto;
import telran.java30.student.dto.StudentNotFoundException;
import telran.java30.student.dto.StudentResponseDto;
import telran.java30.student.dto.StudentUpdateDto;
import telran.java30.student.model.Student;

@Service
public class StudentServiceImpl implements StudentService {
	@Autowired
	StudentRepository studentRepository;

	@Override
	public boolean addStudent(StudentDto studentDto) {
		if(studentRepository.existsById(studentDto.getId())) {
			return false;
		}
		Student student = new Student(studentDto.getId(), studentDto.getName(), studentDto.getPassword());
		studentRepository.save(student);
		return true;
	}

	@Override
	public StudentResponseDto findStudentById(Integer id) {
		Student student = returnStudentById(id);
		return StudentResponseDto.builder().id(id).name(student.getName()).scores(student.getScores()).build();
	}

	@Override
	public StudentResponseDto deleteStudent(Integer id) {
		StudentResponseDto student = findStudentById(id);
		studentRepository.deleteById(id);
		return student;
	}

	@Override
	public StudentDto updateStudent(Integer id, StudentUpdateDto studentUpdateDto) {
		Student student = returnStudentById(id);
		student.setName(studentUpdateDto.getName());
		student.setPassword(studentUpdateDto.getPassword());
		studentRepository.save(student);
		return StudentDto.builder().id(id).name(student.getName()).password(student.getPassword()).build();
	}

	@Override
	public boolean addScoreToStudent(Integer id, ScoreDto scoreDto) {
		Student student = returnStudentById(id);
		boolean flag = student.addScore(scoreDto.getExamName(), scoreDto.getScore());
		studentRepository.save(student);
		return flag;
	}
	
	public Student returnStudentById(Integer id) {
		return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
	}

	private StudentResponseDto studentToStudentResponseDto(Student student) {
		return StudentResponseDto.builder().id(student.getId()).name(student.getName()).scores(student.getScores()).build();
	}
	@Override
	public List<StudentResponseDto> findStudentsByName(String name) {
		return studentRepository.findByName(name).map(this::studentToStudentResponseDto).collect(Collectors.toList());
	}

	@Override
	public long countStudentsByName(String name) {
		return studentRepository.countByName(name);
	}

	@Override
	public List<StudentResponseDto> findStudentsByExamScore(String examName, int minScore) {
		return studentRepository.findByExamScore("scores." + examName, minScore).map(this::studentToStudentResponseDto).collect(Collectors.toList());
	}
}


