CREATE TABLE IF NOT EXISTS courses (
  courseName varchar(50) NOT NULL,
  courseId varchar(16) NOT NULL,
  semester varchar(16) NOT NULL,
  courseStatus varchar (16) NOT NULL,
  year int(11) NOT NULL,
  PRIMARY KEY (courseName)
);

INSERT INTO courses (courseName, courseId, semester, courseStatus, year) VALUES
('Java OO', 'R0046', 'Autumn', 'Ongoing', 1978);
