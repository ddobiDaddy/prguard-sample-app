# prguard-sample-app

PR Guard 파일럿의 **검사 대상** 샘플. 검사 도구 설정이 하나도 없다 — 그게 의도다.

1. 이 폴더를 개인 GitHub 레포로 올린다 (`git init && git add -A && git commit -m init && git push`).
2. PR Guard 관리 화면 → 저장소 → `owner/prguard-sample-app` 등록.
3. GitHub 레포 Settings → Webhooks → `https://<pr-guard>/webhook/github`, Content type `application/json`,
   Secret = 서버의 `PRGUARD_GITHUB_WEBHOOK_SECRET`, 이벤트 "Pull requests".
4. `./make-demo-pr.sh && git push -u origin demo/vulnerable-pr` → PR 생성.
5. PR 의 체크에 `pr-guard` 상태가 **failure** 로 뜨고, Details 링크로 관리 화면 상세가 열리면 성공.
6. (선택) Settings → Branches → main 보호 규칙에 required status check `pr-guard` 추가 → 머지 버튼이 막힌다.

정상 PR 테스트: `PriceCalculator.java` 에 메서드 하나를 추가하는 PR 을 열면 PASS 여야 한다.
