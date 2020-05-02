(ns app.converter)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; converts entry list values (string <---> id)
(defn- get-all-by-key [entry-list key]
  (distinct (map #(key %) entry-list))
)

(defn get-id-map[entry-list key]
  (let [all-privs (get-all-by-key entry-list key)
        ids (range (count all-privs))
      ]
    (merge (zipmap ids all-privs) (zipmap all-privs ids))
  )
)

(defn get-id-req [entry-list]
  (let [names (get-id-map entry-list :name)
        privs (get-id-map entry-list :privilege)]
    (map #(hash-map :name (get names (:name %1)), :privilege (get privs (:privilege %1)) :privilegeType (:privilegeType %1)) entry-list)
  )
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; convert ui-format ---> server-format

(defn- get-req-format [compound element]
    (if (= (:privilegeType element) "Requested")
      (assoc compound :userPrivileges (conj (:userPrivileges compound) {:user (:name element) :privilege (:privilege element)}))
      (assoc compound :forbiddenUserPrivileges (conj (:forbiddenUserPrivileges compound) {:user (:name element) :privilege (:privilege element)}))
    )
  )
  
(defn- get-formatted-req [req]
  (assoc (reduce get-req-format {:userPrivileges [] :forbiddenUserPrivileges []} req) :maxGroups 5)
)

(defn get-ui-data [unformatted-data]
  (get-formatted-req (get-id-req unformatted-data))
)
  
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; convert server-format ---> ui-format

(defn- get-list-of-user-ids [compound k entry-list]
  (assoc compound k (map #(:user %) entry-list))  
)

(defn- get-groups-of-user-ids [entry-list]
  (let [group-map (group-by :group entry-list)]
    (reduce-kv get-list-of-user-ids {} group-map)
  )
)

(defn- get-list-of-user-name-lists [list-of-lists name<->id]
  (map (fn [lst] 
          (map (fn [id] (get name<->id id)) lst)
        ) 
  list-of-lists)
)

(defn get-groups-of-user-names [entry-list name<->id]
  (let [user-id-group (get-groups-of-user-ids entry-list)]
    (zipmap (keys user-id-group) (get-list-of-user-name-lists (vals user-id-group) name<->id))  
  )
)

