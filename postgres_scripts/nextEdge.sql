CREATE OR REPLACE FUNCTION next_edge_no(p_problem_id INT) RETURNS INT AS $$
DECLARE
	rows_no int := 0;
BEGIN
	SELECT COUNT(*) INTO rows_no FROM edges WHERE problem_id = p_problem_id;
	
	RAISE NOTICE 'edge_no: %', rows_no + 1;
	
	RETURN rows_no + 1;
END;
$$ LANGUAGE plpgsql;