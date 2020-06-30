(ns whack-a-mole.core
  (:gen-class))

(def board (atom (vec (repeat 10 (vec (repeat 10 "*"))))))
(def tries (atom 50))
(def moles (atom 10))

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

(defn place-moles
  []
  (loop [n 10]
    (println n)
    (let [x (rand-int 10)
          y (rand-int 10)
          open? (= "*" (get-in @board [x y]))]
      (cond
        (and (pos? n) open?)
        (do
          (swap! board assoc-in [x,y] "M")
          (recur (dec n)))

        (pos? n)
        (recur n)

        :else
        @board))))

(defn whack
  [x y]
  (let [spot (get-in @board [x y])]
    (swap! tries dec)
    (cond
      (= "*" spot) (swap! board assoc-in [x y] "W")
      (= "M" spot)
      (do
        (swap! board assoc-in [x y] "X")
        (swap! moles dec))
      :else @board)))

(defn -main
  "Main function for whackin moles"
  [& args]
  (place-moles)
  (loop [x (ask-input) y (ask-input)]
    (if (and (= x -1) (= y -1))
      (println "Thanks for playing")
      (do
        (whack x y)
        (println @board)
        (recur (ask-input) (ask-input))))))
