CREATE TABLE IF NOT EXISTS metadata (
  id INT NOT NULL,
  data_name VARCHAR(50) NOT NULL,
  data_value VARCHAR(50) NOT NULL,
  PRIMARY KEY (id)
);


INSERT INTO `metadata` (`id`, `data_name`, `data_value`) VALUES
	(1, 'schema_version', '0');

