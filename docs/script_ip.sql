SELECT * FROM integracao_precos.produto_integracao;
SELECT * FROM integracao_precos.produto WHERE is_master = TRUE;
SELECT COUNT(*) FROM integracao_precos.produto;
ALTER TABLE integracao_precos.produto ADD COLUMN is_master BOOLEAN DEFAULT FALSE; -- ESTE
ALTER TABLE integracao_precos.produto ALTER COLUMN is_master SET DEFAULT FALSE;
ALTER TABLE integracao_precos.produto DROP COLUMN is_master;
SELECT p.nome FROM integracao_precos.produto AS p
JOIN integracao_precos.produto_integracao AS pi ON p.id = pi.id_produto
WHERE id_integracao = 1;

SELECT id_integracao FROM integracao_precos.produto_integracao
GROUP BY id_integracao
ORDER BY id_integracao ASC;

SELECT * FROM integracao_precos.produto_integracao WHERE id_integracao = 1 ORDER BY id_produto FETCH FIRST ROW ONLY;

CREATE OR REPLACE FUNCTION integracao_precos.setDefaultIntgProd() RETURNS VOID AS $$
DECLARE
	c1 RECORD;
	c2 integracao_precos.produto_integracao%ROWTYPE;
BEGIN
	FOR c1 IN (SELECT id_integracao FROM integracao_precos.produto_integracao
			  GROUP BY id_integracao
			  ORDER BY id_integracao ASC) LOOP
		SELECT * INTO c2 FROM integracao_precos.produto_integracao WHERE id_integracao = c1.id_integracao ORDER BY id_produto FETCH FIRST ROW ONLY;
		UPDATE integracao_precos.produto SET is_master = TRUE WHERE id = c2.id_produto;
		RAISE NOTICE 'poduto(id %) foi definido como master.', c2.id_produto;
	END LOOP;
END;
$$ LANGUAGE plpgsql;

SELECT integracao_precos.setDefaultIntgProd();

SELECT * FROM integracao_precos.produto;

CREATE OR REPLACE FUNCTION compareStrings(str1 VARCHAR, str2 VARCHAR) RETURNS VOID AS $$
DECLARE
BEGIN
	
END;
$$ LANGUAGE plpgslq;

CREATE OR REPLACE FUNCTION letterPairs(str VARCHAR) RETURNS VARCHAR[] AS $$
DECLARE
	numPairs INTEGER;
	pairs VARCHAR[];
BEGIN
	numPairs := LENGTH(str);
	LOOP
		EXIT WHEN numPairs <= 0;
		
	END LOOP;
END;
$$ LANGUAGE plpgslq;

CREATE OR REPLACE FUNCTION func1() RETURNS VOID AS $$
DECLARE
	c RECORD;
BEGIN
	FOR c IN (SELECT * FROM integracao_precos.produto) LOOP
		IF LEFT(c.nome, 1) = '@'::CHAR(1) THEN
			UPDATE integracao_precos.produto SET nome = '' WHERE id=c.id;
			RAISE NOTICE 'id: %', c.id;
		ELSEIF LEFT(c.marca, 1) = '@'::CHAR(1) THEN
			UPDATE integracao_precos.produto SET marca = '' WHERE id=c.id;
			RAISE NOTICE 'id: %', c.id;
		ELSEIF LEFT(c.modelo, 1) = '@'::CHAR(1) THEN
			UPDATE integracao_precos.produto SET modelo = '' WHERE id=c.id;
			RAISE NOTICE 'id: %', c.id;
		END IF;
	END LOOP;
END;
$$ LANGUAGE PLPGSQL;

