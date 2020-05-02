(ns app.db-state
    (:require [re-frame.core :as rf]))


(rf/reg-event-db              
    :initialize                 
    (fn [_ _]                   
      {:entry-list-content []}
      {:person-content ""}
      {:privilege-content ""}
      {:privilege-type-content "Requested"}
      {:solution-map {}}
      {:user-id-map {}}
      {:privilege-id-map {}}
    ))         