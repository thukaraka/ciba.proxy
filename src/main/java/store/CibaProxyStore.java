/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package store;

/**
 * Store of CIBA Proxy Server.
 */
public class CibaProxyStore {

    private ClientStore clientStore;
    private UserStore userStore;

    private CibaProxyStore() {

        clientStore = ClientStore.getInstance();
        userStore = UserStore.getInstance();
    }

    private static CibaProxyStore cibaProxyStoreInstance = new CibaProxyStore();

    public static CibaProxyStore getInstance() {

        if (cibaProxyStoreInstance == null) {

            synchronized (CibaProxyStore.class) {

                if (cibaProxyStoreInstance == null) {

                    /* instance will be created at request time */
                    cibaProxyStoreInstance = new CibaProxyStore();
                }
            }
        }
        return cibaProxyStoreInstance;

    }

    public ClientStore clientStore() {

        return clientStore;
    }

    public UserStore userStore() {

        return userStore;
    }
}
