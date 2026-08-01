# Autonomous run report

## Executive summary

Autonomous migration of petclinic-rest-v1:
success: shipped, route 200, 6 _array. Findings delta and per-task detail: migration/run-log.md;
debt: migration/debt.md. Orchestrator custom:maas-m2/minimax-m2,
worker qwen27b/qwen3-6-27b, 51 model sessions.

- Outcome: success: shipped, route 200, 6 _array
- Supervisor version: 53c22dd5; run base: 520e17019e51107b16d1c086019949e1c5ad8e92
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
| retro | 88 | rc=0 |
| T-002-sfix | 736 | rc=0 |
| T-004-sfix | 738 | rc=0 |
| T-005-a1p0 | 579 | rc=0 |
| T-007-a1p0 | 290 | rc=0 |
| T-007-a1p1 | 415 | rc=0 |
| T-007-sfix | 902 | rc=124 |
| T-009-a1p0 | 50 | rc=137 |
| T-011-a1p0 | 346 | rc=0 |
| T-011-sfix | 901 | rc=124 |
| T-012-a1p0 | 628 | rc=0 |
| T-013-a1p0 | 665 | rc=0 |
| T-015-a1p0 | 545 | rc=0 |
| T-015-sfix | 903 | rc=124 |
| treefix | 80 | rc=0 |
| T-016-a1p0 | 1336 | rc=0 |
| T-017-a1p0 | 573 | rc=130 |
| T-017-a2p0 | 64 | rc=130 |
| T-018-a1p0 | 514 | rc=130 |
| T-018-a2p0 | 21 | rc=130 |
| T-019-a1p0 | 205 | rc=130 |
| T-019-a2p0 | 45 | rc=130 |
| m5-evaluate-a1p0 | 72 | rc=0 |
| m5-evaluate-sfix | 903 | rc=124 |
| preflightfix-r1-a1p0 | 535 | rc=0 |
| preflightfix-r1-a2p0 | 282 | rc=0 |
| preflightfix-r1-a2p1 | 902 | rc=124 |
| preflightfix-r2-a1p0 | 497 | rc=0 |
| preflightfix-r2-a1p1 | 643 | rc=0 |
| preflightfix-r2-a2p1 | 437 | rc=0 |
| buildfix-r1-a1p0 | 220 | rc=0 |
| gatefix-r1-a1p0 | 317 | rc=0 |
| gatefix-r1-a1p1 | 2654 | rc=0 |
| gatefix-r1-sfix | 902 | rc=124 |
| preflightfix-r1-a1p0 | 250 | rc=0 |
| preflightfix-r2-a1p0 | 902 | rc=124 |
| deployfix-r1-a1p0 | 582 | rc=0 |
| deployfix-r2-a1p0 | 17 | rc=130 |
| deployfix-r2-a2p0 | 161 | rc=137 |
| retro | 60 | rc=0 |
| preflightfix-r1-a1p0 | 227 | rc=0 |

- Escalations (KPI, from supervisor events): 0 (untested: 0)

## Classified events

```
     19 success
     15 escalation_cause
     11 sensor_red_post_commit
     10 no_commit
      9 worker_wedge_class
      8 style_autofix
      8 debt_recorded
      6 quota
      6 preflight_red
      6 already_complete
      4 pipeline_failed
      3 sfix_committed_still_red
      3 pipeline_succeeded
      2 timeout
      2 rule:springboot-annotations-to-quarkus-00002
      2 rule:javax-to-jakarta-import-00001
      2 mechanical_commit
      2 k12_refuted
      1 story_gate_pass
      1 slow_session
      1 sfixscope_reset
      1 sensor_red_at_entry
      1 scope_violation
      1 orphan_worker
      1 debt_retained
      1 acceptance_pass
```

## Per-rule outcomes (K11)

| rule | outcomes |
|---|---|
| `javax-to-jakarta-import-00001` | worker_green, escalation |
| `springboot-annotations-to-quarkus-00002` | worker_green, exhausted |
