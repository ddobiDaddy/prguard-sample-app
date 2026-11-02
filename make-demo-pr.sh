#!/usr/bin/env bash
# "일부러 나쁜 PR" 브랜치를 만들어 PR Guard 가 실제로 잡아내는지 확인한다.
#
#   ./make-demo-pr.sh            (main 에서 실행)
#   git push -u origin demo/vulnerable-pr
#   → GitHub 에서 main 으로 PR 생성 → PR Guard 관리 화면에서 결과 확인
#
# 기대 결과: FAIL. 차단 사유(diff 범위 HIGH)로 아래가 잡혀야 한다.
#   UserDao.java                   SQL 인젝션 / 하드코딩 비밀번호 / MD5 / 약한 난수 → Semgrep (자체 룰 + p/java)
#                                  Statement/ResultSet 미종료                         → PMD CloseResource
#   application-local.properties   AWS 액세스 키(가짜)                                → Gitleaks
#   pom.xml                        log4j-core 2.14.1 (Log4Shell)                    → Grype (선언 행이 diff 안)
set -euo pipefail
cd "$(dirname "$0")"

BRANCH="${1:-demo/vulnerable-pr}"
git switch -c "$BRANCH"

cp demo/UserDao.java.txt src/main/java/com/example/demo/UserDao.java

mkdir -p src/main/resources
printf 'aws.region=ap-northeast-2\naws.accessKeyId=%s%s\n' "AKIA" "QX7RZ4M2WN5TPL8K" \
  > src/main/resources/application-local.properties

sed -i.bak '/DEMO-VULN-DEPENDENCY/a\
    <dependency>\
      <groupId>org.apache.logging.log4j</groupId>\
      <artifactId>log4j-core</artifactId>\
      <version>2.14.1</version>\
    </dependency>' pom.xml && rm -f pom.xml.bak

git add -A
git commit -q -m "feat: add UserDao (DEMO - intentionally vulnerable)"
echo "브랜치 '$BRANCH' 생성. 이제: git push -u origin $BRANCH 후 GitHub 에서 PR 을 여세요."
