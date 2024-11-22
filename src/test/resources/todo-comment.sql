DO $$
DECLARE
   todo_key BIGINT;
BEGIN
SELECT nextval('todos_seq') INTO todo_key;
INSERT INTO todos (id, description, title) VALUES (todo_key, 'title', 'this is a todo.');
INSERT INTO comments (id, todo_id, text) VALUES (nextval('comments_seq'), todo_key, 'comment');
END;
$$;



