package com.example.demo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

/**
 * 데모용 "나쁜 PR" 코드. scripts/make-demo-pr.sh 가 src/main/java 로 복사한다.
 * 의도적으로 넣은 문제:
 *  - SQL 인젝션 (문자열 연결로 쿼리 생성)          → FindSecBugs, Semgrep
 *  - 하드코딩된 DB 비밀번호                          → FindSecBugs, Semgrep
 *  - 약한 해시(MD5)                                   → FindSecBugs, Semgrep
 *  - 예측 가능한 난수로 토큰 생성                     → FindSecBugs, Semgrep
 *  - ResultSet/Statement 미종료                       → PMD CloseResource, SpotBugs
 *  - 테스트 없음                                      → diff-cover (변경분 커버리지)
 */
public class UserDao {

  private static final String DB_URL = "jdbc:mysql://localhost:3306/app";

  public String findEmail(String userId) throws SQLException {
    Connection conn = DriverManager.getConnection(DB_URL, "app", "SuperSecret123!");
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT email FROM users WHERE id = '" + userId + "'");
    return rs.next() ? rs.getString(1) : null;
  }

  public byte[] hashPassword(String password) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("MD5");
    return md.digest(password.getBytes());
  }

  public String newSessionToken() {
    return Long.toHexString(new Random().nextLong());
  }
}
