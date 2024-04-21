package com.example.memic.member.domain;

import com.example.memic.member.exception.InvalidMemberException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import java.util.regex.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member {

    private static final String LOCAL_PARTS_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9\\+_-]+(\\.[A-Za-z0-9\\+_-]+)*@";
    private static final String DOMAIN_PATTERN = "[^-][A-Za-z0-9\\+-]+(\\.[A-Za-z0-9\\+-]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern EMAIL_REGEX = Pattern.compile(LOCAL_PARTS_PATTERN + DOMAIN_PATTERN);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Builder
    public Member(final String email, final String password) {
        validateEmail(email);
        this.email = email;
        this.password = password;
    }

    private void validateEmail(String input) {
        if (input == null) {
            throw new InvalidMemberException("이메일은 null일 수 없습니다.");
        }
        if (isNotMatchEmailForm(input)) {
            throw new InvalidMemberException("이메일 형식이 올바르지 않습니다. 입력된 이메일 : " + input);
        }
    }

    private boolean isNotMatchEmailForm(final String value) {
        return !EMAIL_REGEX.matcher(value).matches();
    }
}
