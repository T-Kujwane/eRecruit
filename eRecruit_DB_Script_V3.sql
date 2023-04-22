DROP DATABASE IF EXISTS  recruitment_db;

CREATE SCHEMA recruitment_db;

USE recruitment_db;

CREATE TABLE skill(
	skill_id INT PRIMARY KEY AUTO_INCREMENT,
    skill VARCHAR(50) NOT NULL
);

SAVEPOINT skill_created;

CREATE TABLE recruiter(
	enterprise_nr VARCHAR(30) PRIMARY KEY,
    enterprise_name VARCHAR(50) NOT NULL,
    enterprise_email VARCHAR(50) NOT NULL,
    enterprise_phone_nr	VARCHAR(10) NOT NULL CONSTRAINT check_recruiter_phone_const CHECK(LENGTH(enterprise_phone_nr) = 10)
);

SAVEPOINT recruiter_created;

CREATE TABLE vacancy_type(
	vacancy_type_id INT PRIMARY KEY AUTO_INCREMENT,
    vacancy_type VARCHAR(50) NOT NULL
);

SAVEPOINT vacancy_type_created;

CREATE TABLE applicant(
	applicant_id VARCHAR(13) PRIMARY KEY CONSTRAINT check_applicant_id_const CHECK(LENGTH(applicant_id) = 13),
    first_name VARCHAR(30) NOT NULL,
    middle_name	VARCHAR(30),
    surname VARCHAR(30) NOT NULL,
    email_address VARCHAR(50) NOT NULL UNIQUE,
    phone_nr VARCHAR(10) NOT NULL UNIQUE CONSTRAINT check_applicant_phone_const CHECK(LENGTH(phone_nr) = 10)
);

CREATE TABLE applicant_skill(
	entry_id INT PRIMARY KEY AUTO_INCREMENT,
    skill_id INT,
    applicant_id VARCHAR(13) NOT NULL,
    FOREIGN KEY (applicant_id) REFERENCES applicant(applicant_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skill(skill_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE prefered_vacancy_type(
	entry_id INT PRIMARY KEY AUTO_INCREMENT,
    vacancy_type_id INT NOT NULL,
    applicant_id VARCHAR(13) NOT NULL,
    FOREIGN KEY (applicant_id) REFERENCES applicant(applicant_id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (vacancy_type_id) REFERENCES vacancy_type(vacancy_type_id) ON DELETE CASCADE ON UPDATE CASCADE
);

SAVEPOINT applicant_created;

CREATE TABLE vacancy(
	reference_nr VARCHAR(20) PRIMARY KEY,
    vacancy_description VARCHAR(2000) NOT NULL,
    closing_date DATE DEFAULT(SYSDATE()) NOT NULL,
    vacancy_type_id INT NOT NULL,
    recruiter_enterprise_nr VARCHAR(30) NOT NULL,
    FOREIGN KEY (recruiter_enterprise_nr) REFERENCES recruiter(enterprise_nr) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (vacancy_type_id) REFERENCES vacancy_type(vacancy_type_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE required_skill(
	entry_id INT PRIMARY KEY AUTO_INCREMENT,
    skill_id INT,
    vacancy_reference_nr VARCHAR(20) NOT NULL,
    FOREIGN KEY (vacancy_reference_nr) REFERENCES vacancy(reference_nr) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skill(skill_id) ON DELETE CASCADE ON UPDATE CASCADE
);

SAVEPOINT vacancy_created;

CREATE TABLE qualification_type (
	type_id INT PRIMARY KEY AUTO_INCREMENT,
    type_name VARCHAR(50) NOT NULL
);

CREATE TABLE course(
	course_id INT PRIMARY KEY AUTO_INCREMENT,
    course_name	VARCHAR(50) NOT NULL
);

CREATE TABLE applicant_qualification(
	entry_id INT PRIMARY KEY AUTO_INCREMENT,
    type_id INT NOT NULL,
    course_id INT,
    applicant_id VARCHAR(13) NOT NULL,
    FOREIGN KEY (applicant_id) REFERENCES applicant(applicant_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (type_id) REFERENCES qualification_type(type_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (course_id) REFERENCES course(course_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE required_qualification(
	entry_id INT PRIMARY KEY AUTO_INCREMENT,
    vacancy_reference_nr VARCHAR(20) NOT NULL,
    type_id INT NOT NULL,
    course_id INT NOT NULL,
    FOREIGN KEY (vacancy_reference_nr) REFERENCES vacancy(reference_nr) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (type_id) REFERENCES qualification_type(type_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (course_id) REFERENCES course(course_id) ON DELETE CASCADE ON UPDATE CASCADE
);

SAVEPOINT qualifications_created;

CREATE TABLE qualifying_applicant(
	entry_id INT PRIMARY KEY AUTO_INCREMENT,
    vacancy_reference_nr VARCHAR(20),
    applicant_id VARCHAR(13), 
    date_qualified DATE NOT NULL,
    FOREIGN KEY (applicant_id) REFERENCES applicant(applicant_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (vacancy_reference_nr) REFERENCES vacancy(reference_nr) ON DELETE CASCADE ON UPDATE CASCADE
);

SAVEPOINT application_created;
COMMIT;

/*SAMPLE DATA INSERTION*/

INSERT INTO qualification_type (type_name) VALUES('National Senior Certificate (NSC)');
INSERT INTO qualification_type (type_name) VALUES('(National) Higher Certificate');
INSERT INTO qualification_type (type_name) VALUES('(National) Diploma');
INSERT INTO qualification_type (type_name) VALUES('BTech');
INSERT INTO qualification_type (type_name) VALUES('Advanced Diploma');
INSERT INTO qualification_type (type_name) VALUES('Bachelor\'s Degree');
INSERT INTO qualification_type (type_name) VALUES('Post-Graduate Diploma');
INSERT INTO qualification_type (type_name) VALUES('Honours Degree');
INSERT INTO qualification_type (type_name) VALUES('Masters Degree');
INSERT INTO qualification_type (type_name) VALUES('Doctor of Philosophy (PhD)');

SAVEPOINT qualification_types_inserted;

INSERT INTO course(course_name) VALUES('Matric Subjects');
INSERT INTO course(course_name) VALUES('Electrical Engineering');
INSERT INTO course (course_name) VALUES('Mechanical Engineering');
INSERT INTO course (course_name) VALUES('Mechatronics Engineering');
INSERT INTO course (course_name) VALUES('Civil Engineering');
INSERT INTO course (course_name) VALUES('Chemical Engineering');
INSERT INTO course (course_name) VALUES('Computer Systems Engineering');
INSERT INTO course (course_name) VALUES('Software Engineering');
INSERT INTO course (course_name) VALUES('Computer Science');
INSERT INTO course (course_name) VALUES('Informatics');
INSERT INTO course (course_name) VALUES('Information Technology');
INSERT INTO course (course_name) VALUES('Office Administration');
INSERT INTO course (course_name) VALUES('Human Resources');

SAVEPOINT courses_inserted;

INSERT INTO recruiter VALUES('2002/015527/30', 'Eskom', 'customerservices@eskom.co.za', '0860037566');
INSERT INTO recruiter VALUES('1990/000900/30', 'Transnet', 'enquiries@transnet.net', '0113083000');
INSERT INTO recruiter VALUES('2012/222866/07', 'Nkgwete IT Solutions', 'queries@nkgwete.co.za', '0136561028');

SAVEPOINT recruiters_inserted;

INSERT INTO skill VALUES (1, 'Communication Skills');
INSERT INTO skill (skill) VALUES('Computer Literacy');
INSERT INTO skill (skill) VALUES('Public Speaking');
INSERT INTO skill (skill) VALUES('Advanced Object-Oriented Programming');
INSERT INTO skill (skill) VALUES('Database Management');
INSERT INTO skill (skill) VALUES('Software Version Control');

SAVEPOINT skills_inserted;


INSERT INTO applicant VALUES ('1234567891234', 'THATO', 'KEITH', 'KUJWANE', 'developer.tk_kujwane@outlook.com' , '0694360330');
INSERT INTO applicant VALUES ('1234567891235', 'THOBILE', 'ZWELISHA', 'JIYANE', '222563372@tut4life.ac.za', '0717342872');
INSERT INTO applicant VALUES ('1234567891236', 'TSHIAMO', NULL, 'Mogolane', '219967969@tut4life.ac.za', '0680006695');

SAVEPOINT applicants_inserted;

INSERT INTO applicant_qualification VALUES(1, (SELECT type_id FROM qualification_type WHERE type_name LIKE '%NSC%'), 
	(SELECT course_id FROM course WHERE UPPER(course_name) LIKE 'Matric%'), '1234567891234');
	
INSERT INTO applicant_qualification (type_id, course_id, applicant_id) VALUES((SELECT type_id FROM qualification_type WHERE type_name LIKE '%NSC%'),
	(SELECT course_id FROM course WHERE UPPER(course_name) LIKE 'MATRIC%'), '1234567891235');
		
INSERT INTO applicant_qualification (type_id, course_id, applicant_id) VALUES((SELECT type_id FROM qualification_type WHERE type_name LIKE '%NSC%'), 
	(SELECT course_id FROM course WHERE UPPER(course_name) LIKE 'Matric%'), '1234567891236');

SAVEPOINT applicant_qualififcations_inserted;


INSERT INTO vacancy_type(VACANCY_TYPE) VALUES('Learnership');
INSERT INTO vacancy_type(VACANCY_TYPE) VALUES('Bursary');
INSERT INTO vacancy_type(VACANCY_TYPE) VALUES('Fixed-Term Contract Employment');
INSERT INTO vacancy_type(VACANCY_TYPE) VALUES('Permanent Employment');

SAVEPOINT vacancy_types_inserted;

INSERT INTO prefered_vacancy_type(vacancy_type_id, applicant_id) VALUES((SELECT vacancy_type_id FROM vacancy_type WHERE vacancy_type = 'Bursary'), '1234567891234');
INSERT INTO prefered_vacancy_type(vacancy_type_id, applicant_id) VALUES((SELECT vacancy_type_id FROM vacancy_type WHERE vacancy_type = 'Bursary'), '1234567891235');
INSERT INTO prefered_vacancy_type(vacancy_type_id, applicant_id) VALUES((SELECT vacancy_type_id FROM vacancy_type WHERE vacancy_type = 'Bursary'), '1234567891236');
INSERT INTO prefered_vacancy_type(vacancy_type_id, applicant_id) VALUES((SELECT vacancy_type_id FROM vacancy_type WHERE vacancy_type = 'Learnership'), '1234567891234');
INSERT INTO prefered_vacancy_type(vacancy_type_id, applicant_id) VALUES((SELECT vacancy_type_id FROM vacancy_type WHERE vacancy_type = 'Learnership'), '1234567891235');
INSERT INTO prefered_vacancy_type(vacancy_type_id, applicant_id) VALUES((SELECT vacancy_type_id FROM vacancy_type WHERE vacancy_type = 'Learnership'), '1234567891236');

SAVEPOINT prefered_vacancies_inserted;

INSERT INTO applicant_skill(skill_id, applicant_id) VALUES ((SELECT skill_id FROM skill WHERE skill = 'Advanced Object-Oriented Programming'), '1234567891234');
INSERT INTO applicant_skill(skill_id, applicant_id) VALUES ((SELECT skill_id FROM skill WHERE skill = 'Communication Skills'), '1234567891235');
INSERT INTO applicant_skill(skill_id, applicant_id) VALUES ((SELECT skill_id FROM skill WHERE skill = 'Public Speaking'), '1234567891236');
INSERT INTO applicant_skill(skill_id, applicant_id) VALUES ((SELECT skill_id FROM skill WHERE skill = 'Software Version Control'), '1234567891234');
INSERT INTO applicant_skill(skill_id, applicant_id) VALUES ((SELECT skill_id FROM skill WHERE skill = 'Database Management'), '1234567891235');
INSERT INTO applicant_skill(skill_id, applicant_id) VALUES ((SELECT skill_id FROM skill WHERE skill = 'Computer Skills'), '1234567891236');

SAVEPOINT applicant_skills_inserted;

/*TIRED AT THIS POINT*/

/*INSERTING VACANCIES AND APPLICATIONS*/
INSERT INTO vacancy (reference_nr, vacancy_description, closing_date, vacancy_type_id, recruiter_enterprise_nr) 
	VALUES('ESKOM_BURSARY2023', 
			'Eskom is looking for academically deserving and who are looking to build a carrer within the electrical engineering space.', 
            SYSDATE(), (SELECT vacancy_TYPE_id FROM vacancy_type WHERE vacancy_type = 'Bursary'), 
			(SELECT enterprise_nr FROM recruiter WHERE enterprise_name = 'Eskom'));
            
INSERT INTO required_skill(vacancy_reference_nr) VALUES('ESKOM_BURSARY2023');

/*DONE!*/

COMMIT;
