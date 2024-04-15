import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class DPTable  {
   private int [][]values;
   private TableView<int[]> tableView = new TableView<>();
    private ObservableList<int[]> data = FXCollections.observableArrayList();
    DPTable(){

    }
  DPTable (int [][] values){
      this.values=values;
  }

    public int[][] getValues() {
        return values;
    }

    public void setValues(int[][] values) {
        this.values = values;
    }

    public TableView<int[]> getTableView() {
        return tableView;
    }

    public void setTableView(TableView<int[]> tableView) {
        this.tableView = tableView;
    }

    public ObservableList<int[]> getData() {
        return data;
    }

    public void setData(ObservableList<int[]> data) {
        this.data = data;
    }

    public  Scene printDPTable ( ){
        // Convert the 2D array to ObservableList
        for (int[] row : values) {
            data.add(row);
        }

        // Create TableColumn for each column
        for (int colIndex = 0; colIndex < values[0].length; colIndex++) {
            TableColumn<int[], Number> column = new TableColumn<>("column " + colIndex);
            final int colNo = colIndex;
            column.setCellValueFactory(cellData ->
                    Bindings.createObjectBinding(() -> cellData.getValue()[colNo], new SimpleIntegerProperty())
            );
            column.setMinWidth(100);
            tableView.getColumns().add(column);
        }

        tableView.setItems(data);
        tableView.setMinWidth(100);

        // Apply some CSS styling to the table and cells
        tableView.setStyle("-fx-font-size: 16; -fx-padding: 10;");
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        ScrollPane scrollPane = new ScrollPane(tableView);

        //scrollPane.setFitToWidth(false);
        scrollPane.setFitToHeight(true);

        return new Scene(scrollPane, 800, 600);
    }


}
