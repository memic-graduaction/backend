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

    private static final String EMAIL_REGEX = "^([\\w\\.\\_\\-])*[a-zA-Z0-9]+([\\w\\.\\_\\-])*([a-zA-Z0-9])+([\\w\\.\\_\\-])+@([a-zA-Z0-9]+\\.)+[a-zA-Z0-9]{2,8}$";

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
        return !Pattern.matches(EMAIL_REGEX, value);
    }
}
