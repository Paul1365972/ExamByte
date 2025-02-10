CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    github_id VARCHAR(255) NOT NULL UNIQUE,
    typ VARCHAR(20) NOT NULL CHECK (typ IN ('Student', 'Corrector', 'Organizer'))
);

CREATE TABLE IF NOT EXISTS tests (
    id SERIAL PRIMARY KEY,
    titel VARCHAR(255) NOT NULL,
    start_zeitpunkt TIMESTAMP NOT NULL,
    end_zeitpunkt TIMESTAMP NOT NULL,
    ergebnis_veroeffentlichungs_zeitpunkt TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS questions (
    id SERIAL PRIMARY KEY,
    test_id INTEGER NOT NULL REFERENCES tests(id) ON DELETE CASCADE,
    typ VARCHAR(20) NOT NULL CHECK (typ IN ('MultipleChoice', 'Freitext')),
    fragestellung TEXT NOT NULL,
    maximale_punktzahl INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS questions_mc_item (
    id SERIAL PRIMARY KEY,
    question_id INTEGER NOT NULL REFERENCES questions(id) ON DELETE CASCADE,
    text TEXT NOT NULL,
    korrekt BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS answers (
    id SERIAL PRIMARY KEY,
    student_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    question_id INTEGER NOT NULL REFERENCES questions(id) ON DELETE CASCADE,
    CONSTRAINT unique_answer UNIQUE(student_id, question_id),
    antwort_mc_item INTEGER NULL REFERENCES questions_mc_item(id) ON DELETE CASCADE,
    antwort_text TEXT NULL,
    CHECK ((antwort_mc_item IS NULL AND antwort_text IS NOT NULL) OR (antwort_mc_item IS NOT NULL AND antwort_text IS NULL))
);

CREATE TABLE IF NOT EXISTS korrektur (
    answer_id INTEGER PRIMARY KEY REFERENCES answers(id) ON DELETE CASCADE,
    korrektor_id INTEGER NULL REFERENCES users(id) ON DELETE CASCADE,
    feedback TEXT NOT NULL,
    punkte INTEGER NOT NULL
);
