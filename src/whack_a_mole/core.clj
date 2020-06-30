(ns whack-a-mole.core
  (:gen-class))

(defn ask-input
  []
  (let [[result ex] (try
                      [(Integer/parseInt (read-line)) nil]
                      (catch NumberFormatException e
                        (println "Thats not a good #")
                        [nil e]))]
    (if-not ex
      result
      (recur))))

(defn make-grid
  []
  (vec (repeat 10 (vec (repeat 10 "*")))))

(defn place-moles
  ([board]
   (place-moles board 10))
  ([board n]
   (let [x (rand-int 10)
         y (rand-int 10)
         open? (= "*" (get-in board [x y]))]
     (if (zero? n)
       board
       (recur (assoc-in board [x y] "M") (dec n))))))

(defn whack
  [board x y]
  (let [spot (get-in board [x y])]
    (cond
      (= "*" spot) (assoc-in board [x y] "W")
      (= "M" spot) (assoc-in board [x y] "X")
      :else board)))

(defn -main
  "Main function for whackin moles"
  [& args]
  (let [board (place-moles (make-grid))]
    (loop [board board x (ask-input) y (ask-input)]
      (println board)
      (recur (whack board x y) (ask-input) (ask-input)))))
