package com.example.keycloakdemo.Configs;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.OIDCHttpFacade;
import org.springframework.cache.Cache;

import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;

public class PathBasedKeycloakConfigResolver implements KeycloakConfigResolver {

    private final ConcurrentHashMap<String, KeycloakDeployment> realmCache = new ConcurrentHashMap<>();

    @Override
    public KeycloakDeployment resolve(OIDCHttpFacade.Request request) {
        String path = request.getURI();
        String realm = null;
        int multitenantIndex = path.indexOf("tenant/");
//        if (multitenantIndex == -1) {
//            throw new IllegalStateException("Not able to resolve realm from the request path!");
//        }

        if(multitenantIndex != -1) {
            String arr[] = path.substring(path.indexOf("tenant/")).split("/");
            if(arr.length>1) {
                realm = arr[1];
            }
        }
        if (realm == null) {
            realm = "default";
        }
        if(realmCache.containsKey(realm)){
            return realmCache.get(realm);
        }
        InputStream is = getClass().getResourceAsStream("/"+realm+"-realm.json");
        realmCache.put(realm,KeycloakDeploymentBuilder.build(is));
        return realmCache.get(realm);
    }
}
