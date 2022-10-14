;; source: https://github.com/coldnew/html2hiccup
;;The MIT License (MIT)
;;
;;Copyright (c) 2016 Yen-Chin, Lee <coldnew.tw@gmail.com>
;;
;;Permission is hereby granted, free of charge, to any person obtaining a copy
;;of this software and associated documentation files (the "Software"), to deal
;;in the Software without restriction, including without limitation the rights
;;to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
;;copies of the Software, and to permit persons to whom the Software is
;;furnished to do so, subject to the following conditions:
;;
;;The above copyright notice and this permission notice shall be included in
;;all copies or substantial portions of the Software.
;;
;;THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
;;IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
;;FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
;;AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
;;LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
;;OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
;;THE SOFTWARE.
(ns converter
  (:require [clojure.string :as str]
            [hickory.core   :as hickory]
            [clojure.walk   :as walk]))

;; https://github.com/davidsantiago/hickory/issues/28
(def ^:private whitespace?
  "Is this a string, and does it consist of only whitespace?"
  (every-pred string? (partial re-matches #"\s*")))

(defn ^:private fixup-hiccup
  "Fixup hiccup tree generate by hickory, this function do:
  1. remove wired \\n\\s* in string.
  2. remove all pure whitespace."
  [row]
  (walk/prewalk
   (fn [form]
     (cond
       ;; remove wired \n\s*
       (string? form) (-> form
                          str/trim-newline str/trim
                          (str/replace #"\n\s*" "\n"))
       ;; remove all pure whitespace
       (vector? form) (into [] (remove whitespace? form))
       :else form))
   row))

(defn html->hiccup
  "Convert Html syntax to Hiccup syntax, return result in string."
  [content]
  (-> content
      hickory/parse
      hickory/as-hiccup
      fixup-hiccup
      str
      (str/replace #"\((.*)\)" "$1")    ; remove first/last ()
      (str/replace #"\[" "\n[")         ; start every opening [ on new line
      (str/replace #"\n\[:html" "[:html") ; first line doesn't neet to add newline char
      (str/replace #" \{\}" "")           ; remove empty {}
      (str/replace #"]\s*]" "]]")         ; close ] tag
      (str/replace #"}\s*\"" "}\n\"")    ; add newline between } and ""
      (str/replace #"]]" "]\n]")         ; add newline between ] and ]
      identity))
