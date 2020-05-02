(ns app.main
  
  (:require [reagent.core :as r]
    [reagent.dom :as rdom]
    [re-frame.core :as rf]
    [app.converter :as c]
    [app.db-state]
    [app.db-events]
  ))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; input form

(defn add-entry-button []
  (let [
        name  (rf/subscribe [:show-person-content])
        privilege  (rf/subscribe [:show-privilege-content])
        privilegeType  (rf/subscribe [:show-privilege-type-content])
        lst  (rf/subscribe [:show-entry-list-content])
  ]
    [:button {:type "button" :on-click #(rf/dispatch [:update-entry-list-content (conj @lst {:name @name :privilege @privilege :privilegeType @privilegeType})])} "Add entry"]
  )
)

(defn tf-person []
  (let [vals  (rf/subscribe [:show-person-content])]
    [:input {:type "text" :value @vals :on-change #(rf/dispatch [:update-person-content (-> % .-target .-value)])}]
  )
)

(defn tf-privilege []
  (let [vals  (rf/subscribe [:show-privilege-content])]
    [:input {:type "text" :value @vals :on-change #(rf/dispatch [:update-privilege-content (-> % .-target .-value)])} ]
  )
)

(defn privilege-combo-box []
  (let [vals  (rf/subscribe [:show-privilege-type-content])]
    [:div 
      [:select {:id "privilege-type"  :on-change #(rf/dispatch [:update-privilege-type-content (-> % .-target .-value)]) :value @vals}
        [:option {:value "Requested"} "Requested"]
        [:option {:value "Forbidden"} "Forbidden"]
      ]
    ]  
  )
)

(defn input-form []
  [:form {:id "input-form"}
    [tf-person]
    [tf-privilege]
    [privilege-combo-box]
    [add-entry-button]
  ]
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; entry list
(defn entry [name privilege privilegeType]
  [:div {:class "container"}
    [:div {:class "row"}
    [:div {:class "col s4"} (str name " " privilege " " privilegeType)]
    ]
  ]  
)

(defn data-to-entry [data]
  [entry (:name data) (:privilege data) (:privilegeType data)]  
)

(defn entry-list []
  (let [list  (rf/subscribe [:show-entry-list-content])]
    [:div {:id "entry-list"}
      [:ul (map data-to-entry @list)]
    ]  
  )
)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; solution list

(defn solution-entry [name]
  [:p name]  
)

(defn to-sol-entry [name]
  [solution-entry name]  
)

(defn solution-group [name-list group-id]
  [:li {:class "solution-entry"}
    [:span {:class "title"} (str "Group Id: " group-id)]
    (map to-sol-entry name-list)
  ]  
)

(defn solution-list []
  (let [sol-map (rf/subscribe [:show-solution-map])]
    [:div {:id "solution-list"}
      [:h5 "Solution: "]
      [:ul {:class "collection"}
       (map solution-group (vals @sol-map) (keys @sol-map))
      ]
    ]
  )  
)

(defn solver-btn []
  (let [lst (rf/subscribe [:show-entry-list-content])]
    [:button {:class "waves-light btn" :onClick #(rf/dispatch [:start-solver @lst]) :type "button"} "Solve"]  
  )
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; entry point    

(defn main-app []
  [:div {:className "App"}
    [input-form]
    [entry-list]
    [solver-btn]
    [solution-list]
  ]  
)

(defn ^:export run []
  (rf/dispatch-sync [:initialize])
  (rdom/render [main-app] (js/document.querySelector "#app")))

(run)

