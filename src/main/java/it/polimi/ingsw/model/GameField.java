package it.polimi.ingsw.model;


public class GameField{

//    //private final IslandList islands;
//    private int numberOfIslands = 12;
//
//    public  GameField(UUID gameID, int numberOfPlayers) {
//
//        //Initialize students on the board
//        ArrayList <Color> studentToBePlaced = new ArrayList <>();
//        for (int j = 0; j < 2; j++) {
//            studentToBePlaced.add(Color.RED);
//            studentToBePlaced.add(Color.GREEN);
//            studentToBePlaced.add(Color.BLUE);
//            studentToBePlaced.add(Color.PINK);
//            studentToBePlaced.add(Color.YELLOW);
//        }
//
//        int noStudentTile = (int) Math.floor(Math.random() * (6) + 1);
//
//        //sets the two island without students with a random number
//        ArrayList <Color> students = new ArrayList <>();
//        for (int i = 1; i <= Game.MAX_TILE; i++) {
//            if ((i != noStudentTile) & (i != noStudentTile +6)) {
//                try {
//                    students.add(GameManager.drawFromPool(1, studentToBePlaced).get(0));
//                } catch (NotEnoughElements e) {
//                    e.printStackTrace();
//                }
//            }
//            }
//
//
//        //sets mother nature randomly in one of the two islands without students
//        ArrayList<Integer> randomSelection = new ArrayList<>();
//        randomSelection.add(noStudentTile);
//        randomSelection.add(noStudentTile + 6);
//        Collections.shuffle(randomSelection);
//
//        //islandList.addIslands(islandTiles);
//
//    }
//
//    /**
//     * method used to call mergeIsland giving 2 islandID
//     * @param newMergedIsland ID of the remaining island
//     * @param islandToBeMerged ID of the island that will be merged into the remaining one
//     * @throws InvalidParameterException when ID is not in range 1-12
//     */
//    /*public void mergeIsland(int newMergedIsland, int islandToBeMerged) throws InvalidParameterException, EndGameException {
//            islands.mergeIslands(newMergedIsland, islandToBeMerged);
//            this.decreaseIslands();
//    }*/
//
//
//    /**
//     * method that moves MotherNature to an island that is at a certain number of moves apart from the previous position
//     * @param moves
//     */
//    public void moveMotherNatureWithGivenMoves(int moves){
//        try {
//            islands.moveMotherNatureWithGivenMoves(moves);
//        } catch (EndGameException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * method that moves motherNature to a specified island
//     * @param nodeID
//     */
//    public void moveMotherNatureToNode (int nodeID) throws EndGameException {
//        islands.moveMotherNatureToNodeID(nodeID);
//    }
//
//    public IslandList getIslandList() {
//        return islands;
//    }
//
//
//    public Pouch getPouch() {
//        return pouch;
//    }
//
//
//    public ArrayList <CloudTile> getCloudsTile() {
//        return cloudTiles;
//    }
//
//    private void decreaseIslands() {
//        this.numberOfIslands = numberOfIslands-1;
//    }
//
//    /**
//     * method that returns the Node that contains the islandTile with the given ID
//     * @param nodeID
//     * @return
//     */
//    public Node getIslandNode(int nodeID) {
//        return islands.getIslandNode(nodeID);
//    }
//
//    /**
//     * method that returns the ArrayList of students inside the node with motherNature on
//     * @return
//     */
//    public ArrayList<Color> getMotherNatureArrayList() {
//        return this.getIslandList().getMotherNature().getStudents();
//    }
//
//    /**Method that modify the amount of coin on the gamefield due to a draw by a player
//     * @param quantity amount of money to add or remove
//     * */
//    public void setCoins(int quantity){
//        this.coins = this.coins + quantity;
//    }
//
//    /**
//     * method that returns the Node containing motherNature
//     * @return
//     */
//    public Node getMotherNatureNode() {
//        return this.getIslandList().getMotherNature();
//    }
//
//    public int getCoins() {
//        return coins;
//    }
//
//    public boolean isStopped (int islandID) {
//        return this.getIslandList().getIslandNode(islandID).isStopped();
//    }
//
//    public int getIslandID(int islandID) {
//        return this.getIslandList().getIslandNode(islandID).getNodeID();
//    }
}
