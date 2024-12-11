(ns com.esigs.dora.config.interface
  (:require [com.esigs.dora.config.core :as core]))

(defn config []
  (core/config))

(defn app-config []
  (core/app-config))
