(ns com.esigs.dora.config.core
  (:import  [java.io File])
  (:require [com.esigs.ezutils.env :as env]
            [com.esigs.ezutils.file :as f]))

(def defaults {:app-name "dora"
               :sc [{:name :git}
                    {:name :file
                     :file-name ".dora" }]})

(defn config-file []
  (let [h (:user-home(env/config))
        s (str h "/.config/esigs.dora/config.edn")]
    s))

(defn load-app-config []
  (let [s (config-file)]
    (if (f/file-exists? s)
      {::app-config (f/read-file->edn s)}
      (f/dir-spit s defaults))))

(defn config []
  (merge 
    (load-app-config)
    (env/config)))

(defn app-config []
  (::app-config (config)))
