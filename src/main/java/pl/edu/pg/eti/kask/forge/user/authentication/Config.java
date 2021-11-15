package pl.edu.pg.eti.kask.forge.user.authentication;

import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;
import javax.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import javax.security.enterprise.authentication.mechanism.http.FormAuthenticationMechanismDefinition;
import javax.security.enterprise.authentication.mechanism.http.LoginToContinue;
import javax.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;

@ApplicationScoped
@BasicAuthenticationMechanismDefinition(realmName = "Forge")
@DatabaseIdentityStoreDefinition(
        dataSourceLookup = "jdbc/ForgeEq",
        callerQuery = "select password from users where login = ?",
        groupsQuery = "select role from users_roles where user = ?",
        hashAlgorithm = Pbkdf2PasswordHash.class
)
public class Config {
}
