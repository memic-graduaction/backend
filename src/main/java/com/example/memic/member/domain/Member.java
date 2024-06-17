package com.example.memic.member.domain;

import com.example.memic.member.exception.InvalidMemberException;
import com.example.memic.member.exception.InvalidPasswordException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import java.util.Objects;
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

    @Transient
    public static final Member NON_MEMBER = new Member(1L, "non-member@memic.com", "non-member");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Email
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    private Member(final Long id, final String email, final String password) {
        validateEmail(email);
        this.id = id;
        this.email = email;
        this.password = password;
    }

    @Builder
    public Member(final String email, final String password) {
        this(null, email, password);
      
    public void updatePassword(final String password) {
        this.password = password;
    }

    public void checkPassword(final String inputPassword) {
        if (!isEqualsPassword(inputPassword)) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }
    }

    private boolean isEqualsPassword(final String inputPassword) {
        return this.password.equals(inputPassword);
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Member member = (Member) o;
        return Objects.equals(getEmail(), member.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail());
    }
}
