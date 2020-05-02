(ns app.db-events
    (:require [re-frame.core :as rf]
        [app.converter :as c]
        [day8.re-frame.http-fx]
        [ajax.core :as ajax] 
        ))

(rf/reg-event-db                 
    :update-entry-list-content                         
    (fn [db [_ value]]          
    (assoc db :entry-list-content value)))
  
(rf/reg-event-db                 
:update-person-content                         
(fn [db [_ value]]          
(assoc db :person-content value)))  

(rf/reg-event-db                 
  :update-privilege-content                         
  (fn [db [_ value]]          
  (assoc db :privilege-content value)))  

(rf/reg-event-db                 
  :update-privilege-type-content                         
  (fn [db [_ value]] 
  (assoc db :privilege-type-content value)))  

(rf/reg-event-db
  :update-on-solution-found
  (fn [db [_ result]]
    (assoc db :solution-map (c/get-groups-of-user-names (:userInGroupList result) (c/get-id-map (:entry-list-content db) :name)))
  )
)
  
  (rf/reg-event-db
    :update-on-solution-missing
    (fn [db [_ result]]
      (assoc db :solution-map {})
    )
  )
  
  (rf/reg-event-fx
    :start-solver
    (fn [_world [_ val]]
      {:http-xhrio {:method          :post
                    :uri             "http://localhost:8080/solve"
                    :params          (c/get-ui-data val)
                    :timeout         5000
                    :format          (ajax/json-request-format)
                    :response-format (ajax/json-response-format {:keywords? true})
                    :on-success      [:update-on-solution-found]
                    :on-failure      [:update-on-solution-missing]}}))