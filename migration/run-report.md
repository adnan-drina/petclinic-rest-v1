# Autonomous run report

## Executive summary

Autonomous migration of petclinic-rest-v1:
story gate passed (non-deploy story): pipeline + quality gate green. Findings delta and per-task detail: migration/run-log.md;
debt: migration/debt.md. Orchestrator custom:maas-m2/minimax-m2,
worker qwen27b/qwen3-6-27b, 10 model sessions.

- Outcome: story gate passed (non-deploy story): pipeline + quality gate green
- Supervisor version: 1292ec6f; run base: 8c0962b609b7403744704e35033331ac7675b6fc
- Orchestrator: custom:maas-m2/minimax-m2; worker: qwen27b/qwen3-6-27b

## Sessions

| session | seconds | rc |
|---|---|---|
| T-001-a1p0 | 196 | rc=0 |
| T-003-a1p0 | 253 | rc=0 |
| T-004-sfix | 445 | rc=130 |
| T-005-a1p0 | 139 | rc=130 |
| T-005-a2p0 | 17 | rc=130 |
| T-007-sfix | 643 | rc=0 |
| T-008-sfix | 718 | rc=0 |
| m5-evaluate-a1p0 | 299 | rc=0 |
| preflightfix-r1-a1p0 | 903 | rc=124 |
| preflightfix-r1-a2p0 | 264 | rc=0 |

- Escalations (KPI, from supervisor events): 0 (untested: 0)

## Classified events

```
      5 success
      3 sensor_red_post_commit
      2 style_autofix
      2 rule:springboot-annotations-to-quarkus-00002
      2 rule:javax-to-jakarta-import-00001
      2 escalation_cause
      2 already_complete
      1 timeout
      1 story_gate_pass
      1 sfixscope_reset
      1 preflight_red
      1 pipeline_succeeded
      1 no_commit
```

## Per-rule outcomes (K11)

| rule | outcomes |
|---|---|
| `javax-to-jakarta-import-00001` | worker_green, escalation |
| `springboot-annotations-to-quarkus-00002` | worker_green, exhausted |
