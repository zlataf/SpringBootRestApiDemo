create table  IF NOT EXISTS metadata
(
   id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
   filename VARCHAR(255) NOT NULL,
   filedescription VARCHAR(255),
   filetype VARCHAR(255) NOT NULL, 
   uploaded_ts TIMESTAMP NOT NULL, 
   publicAccess BOOLEAN NOT NULL,
   size BIGINT NOT NULL
);

INSERT INTO metadata (filename, filedescription, filetype, uploaded_ts, publicAccess, size ) VALUES
  ('aee.txt', 'text', 'text/plain', CURRENT_TIMESTAMP(),  true,2345),
  ('rrb.jpg', 'picture', 'image/jpeg',CURRENT_TIMESTAMP(), false, 567),
  ('uttt.png', 'image', 'image/png',CURRENT_TIMESTAMP(),  true, 234556);