# Instruções para Remoção de Logins Duplicados e Configuração de Índice Único

## Pré-requisitos
Antes de executar estes comandos, certifique-se de ter um backup completo do banco de dados.

## Passo 1: Identificar Usuários com Login Duplicado

Execute esta consulta para verificar se existem logins duplicados:

```sql
-- Identifica logins duplicados
SELECT login, COUNT(*) as quantidade
FROM usuario 
GROUP BY login 
HAVING COUNT(*) > 1
ORDER BY quantidade DESC;
```

## Passo 2: Analisar Usuários Duplicados

Para cada login duplicado, visualize os detalhes dos usuários:

```sql
-- Substitua 'LOGIN_DUPLICADO' pelo login identificado no passo anterior
SELECT id, nome, login, email, cidade, data_nascimento
FROM usuario 
WHERE login = 'LOGIN_DUPLICADO'
ORDER BY id;
```

## Passo 3: Remoção Manual dos Duplicados

**ATENÇÃO**: Analise cuidadosamente quais registros manter e quais remover. 
Considere manter o usuário mais antigo (menor ID) ou aquele com mais dados completos.

### Opção A: Manter o usuário mais antigo (menor ID)
```sql
-- Remove usuários duplicados mantendo apenas o mais antigo
DELETE u1 FROM usuario u1
INNER JOIN usuario u2 
WHERE u1.login = u2.login 
AND u1.id > u2.id;
```

### Opção B: Remoção manual seletiva (recomendado)
```sql
-- Execute para cada usuário duplicado que deve ser removido
-- Substitua XXXX pelo ID do usuário a ser removido
DELETE FROM usuario WHERE id = XXXX;
```

## Passo 4: Verificar se Não Há Mais Duplicados

```sql
-- Deve retornar 0 registros
SELECT login, COUNT(*) as quantidade
FROM usuario 
GROUP BY login 
HAVING COUNT(*) > 1;
```

## Passo 5: Criar Índice Único no Campo Login

Após remover todos os duplicados, execute:

```sql
-- Adiciona índice único no campo login
ALTER TABLE usuario ADD CONSTRAINT UK_usuario_login UNIQUE (login);
```

## Verificação Final

```sql
-- Verifica se o índice único foi criado
SHOW INDEX FROM usuario WHERE Column_name = 'login';
```

## Rollback em Caso de Problemas

Se algo der errado, restaure o backup criado no início do processo.

## Observações Importantes

1. **Backup**: Sempre faça backup antes de executar operações de remoção
2. **Análise**: Analise cuidadosamente quais usuários manter em caso de duplicatas
3. **Relacionamentos**: Verifique se existem tabelas relacionadas (como partidas) que podem ser afetadas
4. **Teste**: Execute primeiro em ambiente de desenvolvimento/teste
5. **Horário**: Execute em horário de menor movimento do sistema

## Script Completo para Ambiente de Desenvolvimento

```sql
-- APENAS PARA AMBIENTE DE DESENVOLVIMENTO/TESTE
-- Remove duplicados mantendo apenas o usuário mais antigo
DELETE u1 FROM usuario u1
INNER JOIN usuario u2 
WHERE u1.login = u2.login 
AND u1.id > u2.id;

-- Adiciona constraint de unicidade
ALTER TABLE usuario ADD CONSTRAINT UK_usuario_login UNIQUE (login);

-- Verifica resultado
SELECT login, COUNT(*) as quantidade
FROM usuario 
GROUP BY login 
HAVING COUNT(*) > 1;
```