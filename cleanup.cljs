(ns cleanup
  (:require ["svgo" :as svgo]
            ["fs" :as fs]
            ["path" :as path]
            [converter :refer [html->hiccup]]


            ;; [taipei-404.html :refer [html->hiccup]]
            [nbb.core :refer [*file*]]))

(def settings {:multipass true
               :plugins [{:name "preset-default"
                          :params {:overrides {:removeViewBox false}}}
                         "removeDimensions"
                         "sortAttrs"
                         {:name "convertColors"
                          :params {:currentColor true}}]})

(defn usage []
  (println "usage: <path to svg file>"))
(defn -main
  [& [svg-path]]
  (if (nil? svg-path)
    (usage)
    (let [svg-string (str (fs/readFileSync svg-path))
          settings-js (-> settings (merge {:path svg-path}) clj->js)
          {:keys [data path info]} (-> svg-string (svgo/optimize settings-js) (js->clj   {:keywordize-keys true}))]
      (println (-> r :data html->hiccup))
      )))
