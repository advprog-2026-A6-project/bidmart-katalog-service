# Test and Quality Reports

This module publishes its quality and testing evidence in two ways:

## 1. CI artifacts

Every CI run uploads the following artifacts:

- `build/reports/tests/test` for the JUnit report
- `build/reports/jacoco/test` for the JaCoCo report
- `build/site/serenity` for the Serenity functional-test report

These artifacts are available from the GitHub Actions run summary for the `CI` workflow.

## 2. Public GitHub Pages report bundle

The `Publish Test Reports` workflow generates and publishes a static site that contains:

- `JaCoCo Coverage Report`
- `JUnit Test Report`
- `Serenity Functional Test Report`

### One-time GitHub Pages setup (required)

If deploy fails with `Failed to create deployment (status: 404)`, Pages is not enabled yet for the repository.

1. Open **Settings → Pages** in the GitHub repo  
   (`https://github.com/advprog-2026-A6-project/bidmart-katalog-service/settings/pages`)
2. Under **Build and deployment → Source**, choose **GitHub Actions** (not “Deploy from a branch”).
3. Save, then re-run the **Publish Test Reports** workflow.

After the workflow succeeds, open the **github-pages** environment link from the workflow run summary.

### Fallback without Pages

Every workflow run also uploads a `catalog-test-reports` artifact (JUnit + JaCoCo + Serenity folders) on the Actions run page, even when Pages deploy is not configured yet.

## Local generation

To generate the reports locally:

```bash
./gradlew clean test functionalTest jacocoTestReport aggregate
```

Output locations:

- JaCoCo: `build/reports/jacoco/test/html/index.html`
- JUnit: `build/reports/tests/test/index.html`
- Serenity: `build/site/serenity/index.html`
