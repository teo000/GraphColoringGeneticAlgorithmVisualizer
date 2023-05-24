DROP TABLE IF EXISTS problem_instances;
DROP TABLE IF EXISTS edges;
DROP TABLE IF EXISTS solutions;
DROP TABLE IF EXISTS candidates;
DROP TABLE IF EXISTS nodes;


CREATE TABLE problem_instances(
	id SERIAL PRIMARY KEY,
	name TEXT UNIQUE,
	nodes_no int NOT NULL,
	edges_no int NOT NULL
);

CREATE TABLE edges(
	problem_id int,
	edge_no int NOT NULL,
	node1 int NOT NULL,
	node2 int NOT NULL,
	PRIMARY KEY(problem_id, edge_no),
	UNIQUE(problem_id, edge_no),
	CONSTRAINT fk_edges_problem_id
		FOREIGN KEY(problem_id)
			REFERENCES problem_instances(id)
);

CREATE TABLE solutions(
	id SERIAL PRIMARY KEY,
	problem_id int,
	mutation_prob DOUBLE PRECISION NOT NULL,
	crossover_prob DOUBLE PRECISION NOT NULL,
	population_size int NOT NULL,
	generations_no int NOT NULL,
	CONSTRAINT fk_solutions_problem_id
		FOREIGN KEY(problem_id)
			REFERENCES problem_instances(id)
);



CREATE TABLE candidates(
	id SERIAL PRIMARY KEY,
	solution_id int,
	generation int ,
	candidate_index int,
	UNIQUE(solution_id, generation, candidate_index),
	CONSTRAINT fk_candidates_solution_id
		FOREIGN KEY(solution_id)
			REFERENCES solutions(id)
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



