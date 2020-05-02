(ns app.db-subscriber
    (:require [re-frame.core :as rf]))

(rf/reg-sub
    :show-person-content
    (fn [db _]     
      (:person-content db))) 
    
(rf/reg-sub
:show-privilege-content
(fn [db _]     
  (:privilege-content db))) 

(rf/reg-sub
:show-privilege-type-content
(fn [db _]     
  (:privilege-type-content db))) 
    
(rf/reg-sub
:show-entry-list-content
(fn [db _]     
  (:entry-list-content db)))    

(rf/reg-sub
  :show-solution-map
  (fn [db _]     
    (:solution-map db)
  )
)