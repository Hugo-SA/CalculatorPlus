# Resumo das Alterações - Login Único

## Alterações Implementadas

### 1. Entidade Usuario
- Adicionado `unique = true` no campo `login` da anotação `@Column`

### 2. Serviço de Usuário
- Implementada validação para impedir login duplicado no método `save()`
- Criada exceção personalizada `DuplicateLoginException`
- Verificação usa o método existente `findByLogin()` do repositório

### 3. Testes
- Atualizado `UsuarioServiceTest` para validar que exceção é lançada para logins duplicados
- Adicionado isolamento de testes com `@Transactional` e limpeza de dados

## Instruções para Banco de Dados

### Antes de aplicar as alterações em produção:

1. **Fazer backup completo do banco**

2. **Identificar duplicados existentes:**
   ```sql
   SELECT login, COUNT(*) FROM usuario GROUP BY login HAVING COUNT(*) > 1;
   ```

3. **Remover duplicados (manter apenas o mais antigo):**
   ```sql
   DELETE u1 FROM usuario u1
   INNER JOIN usuario u2 
   WHERE u1.login = u2.login AND u1.id > u2.id;
   ```

4. **Criar índice único:**
   ```sql
   ALTER TABLE usuario ADD CONSTRAINT UK_usuario_login UNIQUE (login);
   ```

### Arquivo com instruções detalhadas: `INSTRUCOES_LOGIN_UNICO.md`

## Comportamento
- Cadastro de usuário com login já existente retorna erro amigável
- Não alterado fluxo de cadastro existente
- Apenas campo `login` tem unicidade (email continua permitindo duplicatas)
- Validação ocorre antes da tentativa de salvar no banco