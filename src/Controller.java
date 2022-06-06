import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;

public class Controller {

    ObservableList<String> axis = FXCollections.observableArrayList("X", "Y", "Z", "XYZ");



    NumberAxis xAxis = new NumberAxis();
    NumberAxis yAxis = new NumberAxis();

    XYChart.Series series = new XYChart.Series();
    XYChart.Series series1 = new XYChart.Series();
    XYChart.Series series2 = new XYChart.Series();

    CentralGP centralGP;
    NormalGP normalGP;
    AnomalGP anomalGP;



    @FXML
    private LineChart<Number,Number> linechart =
            new LineChart<Number,Number>(xAxis,yAxis);

    @FXML
    private Button btnRun;

    @FXML
    private ChoiceBox<String> choiseBox = new ChoiceBox<>(axis);
    @FXML
    private CheckBox centralCheck;

    @FXML
    private CheckBox normalCheck;

    @FXML
    private CheckBox anomalCheck;

    @FXML
    void RunOrbit(ActionEvent event) {
        series = new XYChart.Series();
        series.setName("Орбита спутника y от x");
        yAxis = new NumberAxis(0, 3e7, 5e6);
        linechart.getData().clear();
            for (int i = 0; i < anomalGP.result.data[0].length; i++) {
                series.getData().addAll(new XYChart.Data(centralGP.result.data[0][i], centralGP.result.data[1][i]));
            }
            linechart.getData().addAll(series);
    }

    @FXML
    void Run(Event event) throws Exception {
        if (centralCheck.isSelected() && normalCheck.isSelected())
        {
            linechart.getData().clear();
            series = new XYChart.Series();
            if (choiseBox.getValue() == "X")
            {
                series.setName("Невязки между ЦГПЗ и НГПЗ по x");
                for (int i = 0; i < centralGP.result.data[0].length; i++) {
                    series.getData().addAll(new XYChart.Data(i, Math.abs(
                            centralGP.result.data[0][i] - normalGP.result.data[0][i]
                    )));
                }
                linechart.getData().addAll(series);
            }
            if (choiseBox.getValue() == "Y")
            {
                series.setName("Невязки между ЦГПЗ и НГПЗ по y");
                for (int i = 0; i < centralGP.result.data[0].length; i++) {
                    series.getData().addAll(new XYChart.Data<Number, Number>(i, Math.abs(
                            centralGP.result.data[1][i] - normalGP.result.data[1][i]
                    )));
                }
                linechart.getData().addAll(series);
            }
            if (choiseBox.getValue() == "Z")
            {
                series.setName("Невязки между ЦГПЗ и НГПЗ по z");
                for (int i = 0; i < centralGP.result.data[0].length; i++) {
                    series.getData().addAll(new XYChart.Data<Number, Number>(i, Math.abs(
                            centralGP.result.data[2][i] - normalGP.result.data[2][i]
                    )));
                }
                linechart.getData().addAll(series);
            }
            if (choiseBox.getValue() == "XYZ")
            {
                series = new XYChart.Series();
                series.setName("Невязки между ЦГПЗ и НГПЗ по x");
                series1 = new XYChart.Series();
                series1.setName("Невязки между ЦГПЗ и НГПЗ по y");
                series2 = new XYChart.Series();
                series2.setName("Невязки между ЦГПЗ и НГПЗ по z");
                for (int i = 0; i < anomalGP.result.data[0].length; i++) {
                    series.getData().addAll(new XYChart.Data<Number, Number>(i, Math.abs(
                            centralGP.result.data[0][i] - normalGP.result.data[0][i]
                    )));
                    series1.getData().addAll(new XYChart.Data<Number, Number>(i, Math.abs(
                            centralGP.result.data[1][i] - normalGP.result.data[1][i]
                    )));
                    series2.getData().addAll(new XYChart.Data<Number, Number>(i, Math.abs(
                            centralGP.result.data[2][i] - normalGP.result.data[2][i]
                    )));
                }
                linechart.getData().addAll(series, series1, series2);
            }

        }
        else if (centralCheck.isSelected() && anomalCheck.isSelected())
        {
            linechart.getData().clear();
            series = new XYChart.Series();
            if (choiseBox.getValue() == "X")
            {
                series.setName("Невязки между ЦГПЗ и АГПЗ по x");
                for (int i = 0; i < centralGP.result.data[0].length; i++) {
                    series.getData().addAll(new XYChart.Data<Number, Number>(i, Math.abs(
                            centralGP.result.data[0][i] - anomalGP.result.data[0][i]
                    )));
                }
            }
            if (choiseBox.getValue() == "Y")
            {
                    series.setName("Невязки между ЦГПЗ и АГПЗ по y");
                    for (int i = 0; i < centralGP.result.data[0].length; i++) {
                        series.getData().addAll(new XYChart.Data<Number, Number>(i, Math.abs(
                                centralGP.result.data[1][i] - anomalGP.result.data[1][i]
                        )));
                    }
            }
            if (choiseBox.getValue() == "Z")
            {
                    series.setName("Невязки между ЦГПЗ и АГПЗ по z");
                    for (int i = 0; i < centralGP.result.data[0].length; i++) {
                        series.getData().addAll(new XYChart.Data<Number, Number>(i, Math.abs(
                                centralGP.result.data[2][i] - anomalGP.result.data[2][i]
                        )));
                    }
            }
            if (choiseBox.getValue() == "XYZ")
            {
                series = new XYChart.Series();
                series.setName("Невязки между ЦГПЗ и АГПЗ по x");
                series1 = new XYChart.Series();
                series1.setName("Невязки между ЦГПЗ и АГПЗ по y");
                series2 = new XYChart.Series();
                series2.setName("Невязки между ЦГПЗ и АГПЗ по z");
                for (int i = 0; i < anomalGP.result.data[0].length; i++) {
                    series.getData().addAll(new XYChart.Data<Number, Number>(i, Math.abs(
                            centralGP.result.data[0][i] - anomalGP.result.data[0][i]
                    )));
                    series1.getData().addAll(new XYChart.Data<Number, Number>(i, Math.abs(
                            centralGP.result.data[1][i] - anomalGP.result.data[1][i]
                    )));
                    series2.getData().addAll(new XYChart.Data<Number, Number>(i, Math.abs(
                            centralGP.result.data[2][i] - anomalGP.result.data[2][i]
                    )));
                }
                linechart.getData().addAll(series, series1, series2);
            }
            linechart.getData().addAll(series);
        }
        else if (anomalCheck.isSelected() && normalCheck.isSelected())
        {
            linechart.getData().clear();
            series = new XYChart.Series();
            if (choiseBox.getValue() == "X")
            {
                series.setName("Невязки между НГПЗ и АГПЗ по x");
                for (int i = 0; i < centralGP.result.data[0].length; i++) {
                    series.getData().addAll(new XYChart.Data<Number, Number>(i, Math.abs(
                            normalGP.result.data[0][i] - anomalGP.result.data[0][i]
                    )));
                }
            }
            if (choiseBox.getValue() == "Y")
            {
                series.setName("Невязки между НГПЗ и АГПЗ по y");
                for (int i = 0; i < centralGP.result.data[0].length; i++) {
                    series.getData().addAll(new XYChart.Data<Number, Number>(i, Math.abs(
                            normalGP.result.data[1][i] - anomalGP.result.data[1][i]
                    )));
                }
            }
            if (choiseBox.getValue() == "Z")
            {
                series.setName("Невязки между НГПЗ и АГПЗ по z");
                for (int i = 0; i < centralGP.result.data[0].length; i++) {
                    series.getData().addAll(new XYChart.Data<Number, Number>(i, Math.abs(
                            normalGP.result.data[2][i] - anomalGP.result.data[2][i]
                    )));
                }
            }
            if (choiseBox.getValue() == "XYZ")
            {
                series = new XYChart.Series();
                series.setName("Невязки между НГПЗ и АГПЗ по x");
                series1 = new XYChart.Series();
                series1.setName("Невязки между НГПЗ и АГПЗ по y");
                series2 = new XYChart.Series();
                series2.setName("Невязки между НГПЗ и АГПЗ по z");
                for (int i = 0; i < anomalGP.result.data[0].length; i++) {
                    series.getData().addAll(new XYChart.Data<Number, Number>(i, Math.abs(
                            normalGP.result.data[0][i] - anomalGP.result.data[0][i]
                    )));
                    series1.getData().addAll(new XYChart.Data<Number, Number>(i, Math.abs(
                            normalGP.result.data[1][i] - anomalGP.result.data[1][i]
                    )));
                    series2.getData().addAll(new XYChart.Data<Number, Number>(i, Math.abs(
                            normalGP.result.data[2][i] - anomalGP.result.data[2][i]
                    )));
                }
                linechart.getData().addAll(series, series1, series2);
            }
            linechart.getData().addAll(series);
        }
        else if (anomalCheck.isSelected() && centralCheck.isSelected() && normalCheck.isSelected())
        {
            linechart.getData().clear();


        }
    }

    @FXML
    void initialize() throws Exception {
        choiseBox.setItems(axis);
        Satellite s1 = new Satellite(6371.3e3 + 20e6, 0.8, 0, 0, 0, 0, 0, 1);
        s1.calcXYZ(0);

         centralGP = new CentralGP(0, 12*60*60, 30, s1.coords.get(0));
         normalGP = new NormalGP(0, 12*60*60, 30, s1.coords.get(0));
         anomalGP = new AnomalGP(0, 12*60*60, 30, s1.coords.get(0));


        DormanPrinceIntegrator d1 = new DormanPrinceIntegrator();
        d1.setPrecision(1);
        d1.setCorrectStep(false);

        d1.run(centralGP);
        d1.run(normalGP);
        d1.run(anomalGP);

        xAxis.setLabel("t");
        yAxis.setLabel("value");
        linechart.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);
//        linechart.setTitle();
        linechart.setCreateSymbols(false);


    }


}
