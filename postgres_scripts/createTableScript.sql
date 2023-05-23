--DROP TABLE IF EXISTS problem_instances;
--DROP TABLE IF EXISTS edges;
--DROP TABLE IF EXISTS candidates;
--DROP TABLE IF EXISTS nodes;


CREATE TABLE problem_instances(
	id SERIAL PRIMARY KEY,
	name TEXT UNIQUE,
	nodes_no int,
	edges_no int
);

CREATE TABLE edges(
	problem_id int,
	edge_no int,
	node1 int,
	node2 int,
	PRIMARY KEY(problem_id, edge_no),
	CONSTRAINT fk_edges_problem_id
		FOREIGN KEY(problem_id)
			REFERENCES problem_instances(id)
);

CREATE TABLE candidates(
	id SERIAL PRIMARY KEY,
	problem_id int,
	generation int, 
	candidate_index int,
	CONSTRAINT fk_candidates_problem_id
		FOREIGN KEY(problem_id)
			REFERENCES problem_instances(id)
);

CREATE TABLE nodes(
	candidate_id int,
	node int,
	color int,
	PRIMARY KEY (candidate_id, node),
	CONSTRAINT fk_nodes_candidate_id
		FOREIGN KEY(candidate_id)
			REFERENCES candidates(id)
);



