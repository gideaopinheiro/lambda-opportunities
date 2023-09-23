CREATE DATABASE lopportunities;

-- Connect to the database
\c lopportunities;

CREATE TABLE IF NOT EXISTS openings 
(
  id TEXT PRIMARY KEY,
  role TEXT,
  company TEXT,
  remote BOOLEAN,
  link TEXT,
  location TEXT,
  salary NUMERIC
);
