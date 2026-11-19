
CREATE DATABASE library_management;

USE library_management;

CREATE TABLE books (

    id INT AUTO_INCREMENT PRIMARY KEY,

    isbn VARCHAR(20) NOT NULL UNIQUE,

    title VARCHAR(255) NOT NULL,

    author VARCHAR(150) NOT NULL,

    category VARCHAR(100),

    publisher VARCHAR(150),

    publication_year INT,

    total_copies INT NOT NULL DEFAULT 1,

    available_copies INT NOT NULL DEFAULT 1,

    shelf VARCHAR(50)

);


CREATE TABLE members (

    id INT AUTO_INCREMENT PRIMARY KEY,

    name VARCHAR(150) NOT NULL,

    email VARCHAR(150) UNIQUE,

    phone VARCHAR(15),

    address TEXT,

    registration_date DATE DEFAULT (CURRENT_DATE),

    status VARCHAR(20) DEFAULT 'Active'

);


CREATE TABLE book_issues (

    id INT AUTO_INCREMENT PRIMARY KEY,

    member_id INT NOT NULL,

    book_id INT NOT NULL,

    issue_date DATE NOT NULL,

    due_date DATE NOT NULL,

    return_date DATE,

    status VARCHAR(20) DEFAULT 'Issued',

    FOREIGN KEY (member_id)
        REFERENCES members(id),

    FOREIGN KEY (book_id)
        REFERENCES books(id)

);

INSERT INTO books
(isbn, title, author, category, publisher,
 publication_year, total_copies, available_copies, shelf)
VALUES

('9780134685991', 'Effective Java',
 'Joshua Bloch', 'Programming', 'Addison-Wesley',
 2018, 5, 5, 'A-01'),

('9780132350884', 'Clean Code',
 'Robert C. Martin', 'Programming', 'Prentice Hall',
 2008, 4, 4, 'A-02'),

('9781491950296', 'Designing Data-Intensive Applications',
 'Martin Kleppmann', 'Database', 'O Reilly',
 2017, 3, 3, 'B-01'),

('9780262033848', 'Introduction to Algorithms',
 'Thomas H. Cormen', 'Programming', 'MIT Press',
 2009, 5, 5, 'A-03');

SHOW TABLES;


SELECT * FROM books;

SELECT * FROM members;

SELECT * FROM book_issues;
