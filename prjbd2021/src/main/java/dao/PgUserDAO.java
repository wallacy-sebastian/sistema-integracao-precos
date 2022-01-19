/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;

/**
 *
 * @author joao
 */
public class PgUserDAO implements UserDAO {

    private final Connection connection;

    private static final String AUTHENTICATE_QUERY =
                                "SELECT id, nome, nascimento, avatar, cep " +
                                "FROM integracao_precos.usuario " +
                                "WHERE login = ? AND senha = md5(?);";
    
    private static final String CREATE_QUERY =
                                "INSERT INTO integracao_precos.usuario(login, senha, nome, nascimento, avatar) " +
                                "VALUES(?, md5(?), ?, ?, ?);";
    
    private static final String READ_QUERY =
                                "SELECT login, nome, nascimento, avatar, cep " +
                                "FROM integracao_precos.usuario " +
                                "WHERE id = ?;";      
    
    private static final String UPDATE_QUERY =
                                "UPDATE integracao_precos.usuario " +
                                "SET login = ?, nome = ?, nascimento = ? " +
                                "WHERE id = ?;";

    private static final String UPDATE_WITH_PASSWORD_QUERY =
                                "UPDATE integracao_precos.usuario " +
                                "SET login = ?, nome = ?, nascimento = ?, senha = md5(?) " +
                                "WHERE id = ?;";

    private static final String UPDATE_WITH_AVATAR_QUERY =
                                "UPDATE integracao_precos.usuario " +
                                "SET login = ?, nome = ?, nascimento = ?, avatar = ? " +
                                "WHERE id = ?;";

    private static final String UPDATE_WITH_AVATAR_AND_PASSWORD_QUERY =
                                "UPDATE integracao_precos.usuario " +
                                "SET login = ?, nome = ?, nascimento = ?, avatar = ?, senha = md5(?) " +
                                "WHERE id = ?;";

    private static final String DELETE_QUERY =
                                "DELETE FROM integracao_precos.usuario " +
                                "WHERE id = ?;";
    
    private static final String ALL_QUERY =
                                "SELECT id, login " +
                                "FROM integracao_precos.usuario " +
                                "ORDER BY id;";    

    private static final String GET_BY_LOGIN_QUERY =
                                "SELECT id, login, nome, nascimento, avatar, cep " +
                                "FROM integracao_precos.usuario " +
                                "WHERE login = ?;";
    
    public PgUserDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void authenticate(User user) throws SQLException, SecurityException {
        try (PreparedStatement statement = connection.prepareStatement(AUTHENTICATE_QUERY)) {
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getSenha());

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    user.setId(result.getInt("id"));
                    user.setNome(result.getString("nome"));
                    user.setNascimento(result.getDate("nascimento"));
                    user.setCep(result.getString("cep"));
                    user.setAvatar(result.getString("avatar"));
                } else {
                    throw new SecurityException("Login ou senha incorretos.");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgUserDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            throw new SQLException("Erro ao autenticar usuário.");
        }        
    }

    @Override
    public User getByLogin(String login) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(GET_BY_LOGIN_QUERY)) {
            statement.setString(1, login);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    User user = new User();
                    user.setId(result.getInt("id"));
                    user.setNome(result.getString("nome"));
                    user.setNascimento(result.getDate("nascimento"));
                    user.setCep(result.getString("cep"));
                    user.setLogin(login);
                    user.setAvatar(result.getString("avatar"));
                    return user;

                } else {

                    return null;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgUserDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            
            throw new SQLException("Erro ao obter usuário.");
        }
    }

    @Override
    public void create(User user) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getSenha());
            statement.setString(3, user.getNome());
            statement.setDate(4, (Date) user.getNascimento());
            statement.setString(5, user.getCep());
            statement.setString(5, user.getAvatar());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PgUserDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            if (ex.getMessage().contains("uq_user_login")) {
                throw new SQLException("Erro ao inserir usuário: login já existente.");
            } else if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao inserir usuário: pelo menos um campo está em branco.");
            } else {
                throw new SQLException("Erro ao inserir usuário.");
            }
        }
    }

    @Override
    public User read(Integer id) throws SQLException {
        User user = new User();

        try (PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    user.setId(id);
                    user.setLogin(result.getString("login"));
                    user.setNome(result.getString("nome"));
                    user.setNascimento(result.getDate("nascimento"));
                    user.setCep(result.getString("cep"));
                    user.setAvatar(result.getString("avatar"));
                } else {
                    throw new SQLException("Erro ao visualizar: usuário não encontrado.");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgUserDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            
            if (ex.getMessage().equals("Erro ao visualizar: usuário não encontrado.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao visualizar usuário.");
            }
        }

        return user;
    }

    @Override
    public void update(User user) throws SQLException {
        String query;
// PARTE COM AVATAR
        if ((user.getSenha() == null) || (user.getSenha().isBlank())) {
            if ((user.getAvatar() == null) || (user.getAvatar().isBlank()))
                query = UPDATE_QUERY;
            else
                query = UPDATE_WITH_AVATAR_QUERY;
        } else {
            if ((user.getAvatar() == null) || (user.getAvatar().isBlank()))
                query = UPDATE_WITH_PASSWORD_QUERY;
            else
                query = UPDATE_WITH_AVATAR_AND_PASSWORD_QUERY;
        }
// PARTE SEM AVATAR

//        if ((user.getSenha() == null) || (user.getSenha().isBlank())){
//            query = UPDATE_QUERY;
//        } else {
//            query = UPDATE_WITH_PASSWORD_QUERY;
//        }

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getNome());
            statement.setDate(3, user.getNascimento());

//            PARTE COM AVATAR
            if ((user.getSenha() == null) || (user.getSenha().isBlank())) {
                if ((user.getAvatar() == null) || (user.getAvatar().isBlank())) {
                    statement.setInt(4, user.getId());
                } else {
                    statement.setString(4, user.getAvatar());
                    statement.setInt(5, user.getId());
                }
            } else {
                if ((user.getAvatar() == null) || (user.getAvatar().isBlank())) {
                    statement.setString(4, user.getSenha());
                    statement.setInt(5, user.getId());
                } else {
                    statement.setString(4, user.getAvatar());
                    statement.setString(5, user.getSenha());
                    statement.setInt(6, user.getId());
                }
            }
// PARTE SEM AVATAR
//            if ((user.getSenha() == null) || (user.getSenha().isBlank())) {
//                statement.setInt(4, user.getId());
//            } else {
//                statement.setString(4, user.getSenha());
//                statement.setInt(5, user.getId());
//            }

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao editar: usuário não encontrado.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgUserDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            if (ex.getMessage().equals("Erro ao editar: usuário não encontrado.")) {
                throw ex;
            } else if (ex.getMessage().contains("uq_user_login")) {
                throw new SQLException("Erro ao editar usuário: login já existente.");
            } else if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao editar usuário: pelo menos um campo está em branco.");
            } else {
                throw new SQLException("Erro ao editar usuário.");
            }
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id);

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao excluir: usuário não encontrado.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgUserDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            if (ex.getMessage().equals("Erro ao excluir: usuário não encontrado.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao excluir usuário.");
            }
        }
    }

    @Override
    public List<User> all() throws SQLException {
        List<User> userList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(ALL_QUERY);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                User user = new User();
                user.setId(result.getInt("id"));
                user.setLogin(result.getString("login"));

                userList.add(user);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgUserDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            throw new SQLException("Erro ao listar usuários.");
        }

        return userList;
    }
}
