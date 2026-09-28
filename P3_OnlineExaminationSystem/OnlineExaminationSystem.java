package P3_OnlineExaminationSystem;

import java.util.*;

abstract class Question {
    protected String questionText;
    protected int points;

    public Question(String questionText, int points) {
        this.questionText = questionText;
        this.points = points;
    }

    public abstract boolean evaluate(String answer);

    public int getPoints() {
        return points;
    }
}

class MultipleChoiceQuestion extends Question {
    private String correctOption;

    public MultipleChoiceQuestion(
            String questionText,
            int points,
            String correctOption) {

        super(questionText, points);
        this.correctOption = correctOption;
    }

    public boolean evaluate(String answer) {
        return correctOption.equalsIgnoreCase(answer);
    }
}

class TrueFalseQuestion extends Question {
    private boolean correctAnswer;

    public TrueFalseQuestion(
            String questionText,
            int points,
            boolean correctAnswer) {

        super(questionText, points);
        this.correctAnswer = correctAnswer;
    }

    public boolean evaluate(String answer) {
        return Boolean.parseBoolean(answer) == correctAnswer;
    }
}

class ShortAnswerQuestion extends Question {
    private String correctAnswer;

    public ShortAnswerQuestion(
            String questionText,
            int points,
            String correctAnswer) {

        super(questionText, points);
        this.correctAnswer = correctAnswer;
    }

    public boolean evaluate(String answer) {
        return correctAnswer.equalsIgnoreCase(
            answer.trim()
        );
    }
}

class Student {
    private String name;

    public Student(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class Examination {
    private String name;
    private List<Question> questions;

    public Examination(String name) {
        this.name = name;
        this.questions = new ArrayList<>();
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public String getName() {
        return name;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public int getTotalMarks() {

        int total = 0;

        for (Question question : questions) {
            total += question.getPoints();
        }

        return total;
    }
}

enum AttemptStatus {
    IN_PROGRESS,
    SUBMITTED
}

class Attempt {
    private Student student;
    private Examination examination;
    private Map<Integer, String> answers;
    private AttemptStatus status;

    public Attempt(
            Student student,
            Examination examination) {

        this.student = student;
        this.examination = examination;
        this.answers = new HashMap<>();
        this.status = AttemptStatus.IN_PROGRESS;
    }

    public void recordAnswer(
            int questionNumber,
            String answer) {

        if (status == AttemptStatus.SUBMITTED) {
            System.out.println(
                "Cannot change answers for a submitted examination."
            );
            return;
        }

        answers.put(questionNumber, answer);

        System.out.println(
            "Answer recorded for Question " +
            questionNumber + "."
        );
    }

    public void submit() {

        if (status == AttemptStatus.SUBMITTED) {
            System.out.println(
                "Examination is already submitted."
            );
            return;
        }

        status = AttemptStatus.SUBMITTED;

        System.out.println(
            examination.getName() +
            " submitted by " +
            student.getName() +
            "."
        );

        calculateResult();
    }

    private void calculateResult() {

        int totalScore = 0;

        List<Question> questions =
            examination.getQuestions();

        for (int i = 0; i < questions.size(); i++) {

            Question question = questions.get(i);

            String answer =
                answers.get(i + 1);

            boolean correct =
                answer != null &&
                question.evaluate(answer);

            int score =
                correct ? question.getPoints() : 0;

            totalScore += score;

            System.out.println(
                "Result: Question " +
                (i + 1) +
                ": " +
                (correct ? "Correct" : "Incorrect") +
                " (" +
                score +
                " points)"
            );
        }

        System.out.println(
            "Total score: " +
            totalScore +
            "/" +
            examination.getTotalMarks()
        );
    }
}

public class OnlineExaminationSystem {

    public static void main(String[] args) {

        Student student =
            new Student("Student 1");

        Examination exam =
            new Examination("Exam A");

        exam.addQuestion(
            new MultipleChoiceQuestion(
                "Which option is correct?",
                5,
                "C"
            )
        );

        exam.addQuestion(
            new TrueFalseQuestion(
                "Java is an object-oriented language.",
                5,
                false
            )
        );

        System.out.println(
            "Exam A started by Student 1."
        );

        Attempt attempt =
            new Attempt(student, exam);

        attempt.recordAnswer(1, "C");
        attempt.recordAnswer(2, "True");

        attempt.submit();

        attempt.recordAnswer(1, "A");
    }
}