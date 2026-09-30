# CS 2400 — Project 1: Bagsome

Before submitting, return to [README.md](README.md) and complete every required section. README completion is part of the quality score.

## Assignment and team setup

This is a group project and you are required to work in groups.

- The normal team size is **2**. A team of **3** or more requires instructor approval.
- Your team receives one shared GitHub repository and one project score.

Before anyone starts coding:

1. Agree on a group name with your teammates.
2. Open the Classroom 50 assignment link in Canvas while signed in to the GitHub account you registered for this course.
3. Exactly **one** teammate selects **Create group**, enters the agreed name, and becomes the group founder.
4. Every other teammate finds that existing group (**do not create another group**) and selects **Ask to join**.
5. On GitHub, select **Request to join**. The group founder approves the request on the GitHub Team page.
6. Return to Classroom 50 and select **Check again**.
7. Confirm that every member can open the same shared repository before beginning work.

Put the exact Classroom 50 group display name and every member's identity in `README.md`. If a join request is stuck, send the instructor the group name and GitHub usernames. **Do not create another group.**

## Project purpose

Implement three Bag ADT operations in both provided implementations:

- `union(otherBag)`
- `intersection(otherBag)`
- `difference(otherBag)`

Also complete `BagDriver.java` with a useful `main` demonstration.

## What to edit

| Path | What your team should do |
|---|---|
| `src/ResizableArrayBag.java` | Implement `union`, `intersection`, and `difference`. |
| `src/LinkedBag.java` | Implement `union`, `intersection`, and `difference`. |
| `src/BagDriver.java` | Write a clear demonstration of all three operations. |
| `src/BagInterface.java` | Keep method signatures unchanged; edit JavaDoc only if needed to keep the contract accurate. |
| `README.md` | Complete the team, contribution, Big-O, extra-credit, and AI/external-tool sections. This is the only project documentation file your team submits. |

### Instructor-supplied reference files

`PROJECT.md`, `autograder/PublicTestRunner.java`, and the files under `scripts/` are supplied for instructions and local testing. Staff grading uses instructor-controlled copies. If you change your local copies, your local results may differ from the grading results. Passing modified local tests does not show that your submission will pass the instructor's tests.

Do not change `BagInterface` method signatures or unrelated starter behavior merely to make a local test pass.

## Data rules

- Bags do not allow `null` elements.
- An attempt to add `null` must throw `IllegalArgumentException` with a meaningful, non-empty message. The starter implementations already do this.
- Passing `null` as `otherBag` to any of the three operations must also throw `IllegalArgumentException` with a meaningful, non-empty message.

## Functional specifications

The following rules apply to all three operations:

- Do not modify either operand bag.
- Work when `otherBag` uses the other provided implementation.
- Bags are unordered; result order does not matter.
- Support self-operation, such as `bag.union(bag)`.
- Return a new result bag rather than aliasing an input bag.

### `union(otherBag)`

The result contains every item from both bags, including duplicates. Each value's result frequency is the sum of its frequencies in the two operands.

### `intersection(otherBag)`

The result contains values present in both bags. Each value's result frequency is the smaller of its two operand frequencies.

### `difference(otherBag)`

The result represents `this` bag minus `otherBag`. Each matching occurrence in `otherBag` removes at most one occurrence from `this` bag; frequencies never go below zero.

## Documentation requirements

The method contract is documented in `BagInterface.java`. Keep it accurate, including duplicate behavior, return behavior, and exceptions. Do not remove required JavaDoc. Add brief JavaDoc to `BagDriver.main` explaining what the demonstration shows.

Complete every required section in `README.md`.

## Run the public tests locally

Install a **JDK version 17 or newer** and run the tests on your own computer. A JRE alone is not sufficient because the project scripts use `javac`. The grading environment runs Java 17, and the compile scripts use `javac --release 17`; therefore, your source code must use only language features and standard-library APIs available in Java 17, even if you develop with a newer JDK.

You might also want to learn how to run the tests automatically before each local commit.

From the repository root, run the complete readiness check:

macOS, Linux, WSL, or Git Bash:

```bash
bash scripts/check_submission.sh
```

Windows PowerShell:

```powershell
powershell -ExecutionPolicy Bypass -File scripts\check_submission.ps1
```

Windows Command Prompt:

```bat
scripts\check_submission.cmd
```

The readiness check compiles once, runs all 24 matrix cases, runs the null-argument check, prints a summary, and exits nonzero if anything fails. It does not compare commits, files, or checksums.

To run one case while debugging:

```bash
bash scripts/run_public_test.sh RA typical union same
bash scripts/run_public_test.sh L empty difference mixed
bash scripts/run_public_test.sh null-args
```

```powershell
powershell -ExecutionPolicy Bypass -File scripts\run_public_test.ps1 RA typical union same
powershell -ExecutionPolicy Bypass -File scripts\run_public_test.ps1 null-args
```

Arguments for a matrix case:

- implementation: `RA` (ResizableArrayBag) or `L` (LinkedBag)
- case: `typical` or `empty`
- operation: `union`, `intersection`, or `difference`
- mode: `same` or `mixed` implementation types

If you use WSL, install JDK 17 or newer inside WSL; a Windows installation of Java does not automatically provide `javac` there.

## Grading and feedback

The base project is worth **100 points**:

| Component | How it is scored | Points |
|---|---|---:|
| Public functional tests | Raw matrix score below, scaled to 70% | 70 |
| Private functional tests | Additional inputs and edge cases from the published requirements | 10 |
| Quality score | Rubric below | 20 |
| **Base total** |  | **100** |

### Public functional-test matrix

The public tests produce a raw score out of 100. That percentage is scaled to the 70-point public-test component of the project grade.

| Public-test category | Checks and value | Raw points |
|---|---:|---:|
| Typical input, same implementation | 6 × 10 | 60 |
| Typical input, mixed implementations | 6 × 3 | 18 |
| Empty input, same implementation | 6 × 2 | 12 |
| Empty input, mixed implementations | 6 × 1 | 6 |
| Null-argument behavior | 1 × 4 | 4 |
| **Raw total** |  | **100** |

Staff reruns the public matrix from an instructor-controlled copy and also uses private cases that exercise the same published requirements with additional inputs and edge cases. Private tests do not introduce new requirements. The final score shown in Canvas is authoritative.

### Quality score (20 points)

| Quality factor | Full-credit evidence | Points |
|---|---|---:|
| Code quality and design | Clear, focused implementations; appropriate use of `BagInterface`; no needless duplication or unrelated behavior changes | 4 |
| Documentation and analysis | Accurate JavaDoc; complete and justified Big-O analysis; complete `README.md`, including AI/external-tool disclosure | 5 |
| Driver demonstration | `BagDriver.main` clearly demonstrates all three operations with representative data and readable output | 3 |
| Team contribution and attribution | Every member has meaningful, identifiable work; the contribution record agrees with repository evidence; pair or group work is explicitly credited | 4 |
| Development process | Meaningful commits show understandable stages of implementation, testing, revision, and documentation | 4 |
| **Quality total** |  | **20** |

For the **development-process** factor:

- **4 points:** The history shows several meaningful checkpoints, useful commit messages, and a credible progression from initial work through testing and revision.
- **3 points:** The work is generally incremental, but some changes are overly large, clustered near the deadline, or weakly described.
- **2 points:** The history provides limited development evidence; most substantive work appears in one or two large changes.
- **1 point:** The history is minimal or opaque, or meaningful work is poorly attributable among team members.
- **0 points:** All or nearly all substantive work appears in one final bulk commit, or the repository provides no credible development history. Trivial, empty, or artificial commits do not earn process credit.

Commit count alone does not determine this score. A student who works with a teammate through pair or group programming should document that work in `README.md` and use co-authored commits or clear contribution notes when practical.

### Authorship, AI use, and individual accountability

Normally, all members of a team receive the same project score. The instructor may adjust an individual student's score when the evidence shows substantially different participation, misrepresented contributions, or an academic-integrity violation.

Any team member may be asked to complete a brief individual code review. A review may ask the student to:

- identify and explain their contributions;
- explain an operation's duplicate handling, non-mutation behavior, and Big-O cost;
- trace or debug a test case; or
- make a small, closely related code change.

A single late or bulk commit is not, by itself, proof of unauthorized AI use. However, an opaque history—including all substantive work appearing in one final commit—may receive **0/4** for development process and may trigger an individual code review.

AI and other external tools may be used only as allowed by the course syllabus and must be disclosed as required in `README.md`. Disclosure does not make otherwise prohibited use permissible. Every student remains responsible for, and must be able to explain and modify, all work submitted under their name.

If a required review and the other available evidence establish unauthorized AI/external assistance or misrepresented authorship, the responsible student's project grade may be reduced up to and including **zero**, consistent with the course syllabus and [CPP Academic Integrity policy](https://www.cpp.edu/student-affairs/topics/policies.shtml). Confirmed academic-integrity incidents are reported to Student Conduct and Integrity as required by university policy.

Optional extra-credit work must be complete, documented in `README.md`, and runnable from a fresh clone. Do not assume an added test framework will be run by the supplied scripts; include exact run instructions with any extra tests.

## Submit

1. Run the complete local readiness check.
2. Complete and proofread `README.md`.
3. Commit all intended work and push it to the shared repository's default branch before the deadline.
4. Open the repository on GitHub and confirm that the final commit appears there.

## Troubleshooting

- `java` or `javac` not found: install **JDK 17 or newer**, not only a JRE.
- `error: release version 17 not supported`: your JDK is older than 17; install JDK 17 or newer and make sure the newer `javac` is on your `PATH`.
- Permission denied for a Bash script: run `chmod +x scripts/*.sh`, or invoke it with `bash` as shown above.
- Teammate cannot see the repository: verify that they requested membership in the correct GitHub Team, that the founder approved it, and that they selected **Check again** in Classroom 50.
- Duplicate group created: stop using both repositories and contact the instructor immediately; do not try to merge the Classroom 50 groups yourself.
