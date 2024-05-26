package com.example.diploma;

import javafx.beans.NamedArg;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Iterator;

public class GanttChartController<X,Y> extends XYChart<X,Y> {

    ArrayList<StackPane> regions=new ArrayList<>();
    ArrayList<Pane> panes=new ArrayList<>();
    ArrayList<Double> widths=new ArrayList<>();
    ArrayList<Double> xArray=new ArrayList<>();
    ArrayList<Double> yArray=new ArrayList<>();
    ArrayList<ArrayList<Integer>> childrenId=new ArrayList<>();
    double height=0;
    boolean f=false;

    public static class ExtraData {

        public double length;
        //LocalDate lb = LocalDate.parse("2018-10-19");
        //public String outputDate = lb.toString();
        public String styleClass;
        public ArrayList<Integer> childrenArrayId;


        public ExtraData(double lengthMS, String styleClass,ArrayList<Integer> childrenArrayId) {
            super();
            this.length = lengthMS;
            this.styleClass = styleClass;
            this.childrenArrayId=new ArrayList<>(childrenArrayId);
        }
        public double getLength() {
            return length;
        }
        public void setLength(long length) {
            this.length = length;
        }
        public String getStyleClass() {
            return styleClass;
        }
        public void setStyleClass(String styleClass) {
            this.styleClass = styleClass;
        }
        public ArrayList<Integer> getChildrenArrayId() {
            return childrenArrayId;
        }
        public void setChildrenArrayId(ArrayList<Integer> childrenArrayId) {
            this.childrenArrayId = new ArrayList<>(childrenArrayId);
        }
    }

    private double blockHeight = 30;

    public GanttChartController(@NamedArg("xAxis") Axis<X> xAxis, @NamedArg("yAxis") Axis<Y> yAxis) {
        this(xAxis, yAxis, FXCollections.<Series<X, Y>>observableArrayList());
    }

    public GanttChartController(@NamedArg("xAxis") Axis<X> xAxis, @NamedArg("yAxis") Axis<Y> yAxis, @NamedArg("data") ObservableList<Series<X,Y>> data) {
        super(xAxis, yAxis);
        if (!(xAxis instanceof DateAxis && yAxis instanceof CategoryAxis)) {
            throw new IllegalArgumentException("Axis type incorrect, X and Y should both be NumberAxis");
        }
        setData(data);
    }

    private static ArrayList<Integer> getChildrenArrayId(Object obj) {
        return ((ExtraData) obj).getChildrenArrayId();
    }
    private static String getStyleClass( Object obj) {
        return ((ExtraData) obj).getStyleClass();
    }

    private static double getLength( Object obj) {
        return ((ExtraData) obj).getLength();
    }

    @Override protected void layoutPlotChildren() {

        for (int seriesIndex=0; seriesIndex < getData().size(); seriesIndex++) {

            Series<X,Y> series = getData().get(seriesIndex);

            Iterator<Data<X,Y>> iter = getDisplayedDataIterator(series);

            while(iter.hasNext()) {
                Data<X,Y> item = iter.next();
                double x = getXAxis().getDisplayPosition(item.getXValue());
                double y = getYAxis().getDisplayPosition(item.getYValue());
                if (Double.isNaN(x) || Double.isNaN(y)) {
                    continue;
                }

                //if (x < d1.getTime()){
                //  count = getXAxis().getTickLength();
                //}
                Node block = item.getNode();
                Rectangle ellipse;
                if (block != null) {
                    if (block instanceof StackPane) {
                        StackPane region = (StackPane)item.getNode();
                        if (region.getShape() == null) {
                            ellipse = new Rectangle( getLength(item.getExtraValue()), getBlockHeight());
                        } else if (region.getShape() instanceof Rectangle) {
                            ellipse = (Rectangle)region.getShape();
                        } else {
                            return;
                        }



                        ellipse.setArcWidth(30.0);
                        ellipse.setArcHeight(20.0);
                        ellipse.setWidth(getLength( item.getExtraValue()) * ((getXAxis() instanceof NumberAxis) ? Math.abs(((NumberAxis)getXAxis()).getScale()) : 1));
                        //ellipse.setWidth(((getWidth() - 120)/(d.getdiff()*1.15))*20);
                        ellipse.setHeight(getBlockHeight() * ((getYAxis() instanceof NumberAxis) ? Math.abs(((NumberAxis)getYAxis()).getScale()) : 1));

                        y -= getBlockHeight() / 2.0;

                        // Note: workaround for RT-7689 - saw this in ProgressControlSkin
                        // The region doesn't update itself when the shape is mutated in place, so we
                        // null out and then restore the shape in order to force invalidation.
                        region.setShape(null);
                        region.setShape(ellipse);
                        region.setScaleShape(false);
                        region.setCenterShape(false);
                        region.setCacheShape(false);

                        widths.add(ellipse.getWidth());
                        height=ellipse.getHeight();
                        xArray.add(x);
                        yArray.add(y);
                        childrenId.add(getChildrenArrayId(item.getExtraValue()));

                        block.setLayoutX(x);
                        block.setLayoutY(y);
                    }
                }
            }
        }

        for (int i = 0; i < widths.size(); i++) {
            if(childrenId.get(i).get(0)!=-1){
                for (int j = 0; j < childrenId.get(i).size(); j++) {
                    int chId=childrenId.get(i).get(j);

                    Line line = new Line(xArray.get(i)+116+widths.get(i),yArray.get(i)+40+height/2,xArray.get(chId)+116+widths.get(chId)/2,yArray.get(i)+40+height/2);

                    Pane arrowPane = new Pane();

                    if(yArray.get(i)+40+height/2<yArray.get(chId)+40){
                        drawArrowLine(xArray.get(chId)+116+widths.get(chId)/2, yArray.get(i)+40+height/2, xArray.get(chId)+116+widths.get(chId)/2, yArray.get(chId)+40, arrowPane);
                    }
                    else {
                        drawArrowLine(xArray.get(chId)+116+widths.get(chId)/2, yArray.get(i)+40+height/2, xArray.get(chId)+116+widths.get(chId)/2, yArray.get(chId)+40+height, arrowPane);
                    }

                    Pane root = new Pane();
                    root.getChildren().addAll(line,arrowPane);
                    panes.add(root);

                    this.getChildren().add(panes.get(panes.size()-1));
                }
            }
        }
    }

    public static void drawArrowLine(double startX, double startY, double endX, double endY, Pane pane) {
        // get the slope of the line and find its angle
        double slope = (startY - endY) / (startX - endX);
        double lineAngle = Math.atan(slope);

        double arrowAngle = startX > endX ? Math.toRadians(100) : -Math.toRadians(40);

        Line line = new Line(startX, startY, endX, endY);

        double lineLength = Math.sqrt(Math.pow(startX - endX, 2) + Math.pow(startY - endY, 2));
        double arrowLength = 8;

        // create the arrow legs
        Line arrow1 = new Line();
        arrow1.setStartX(line.getEndX());
        arrow1.setStartY(line.getEndY());
        arrow1.setEndX(line.getEndX() + arrowLength * Math.cos(lineAngle - arrowAngle));
        arrow1.setEndY(line.getEndY() + arrowLength * Math.sin(lineAngle - arrowAngle));

        Line arrow2 = new Line();
        arrow2.setStartX(line.getEndX());
        arrow2.setStartY(line.getEndY());
        arrow2.setEndX(line.getEndX() + arrowLength * Math.cos(lineAngle + arrowAngle));
        arrow2.setEndY(line.getEndY() + arrowLength * Math.sin(lineAngle + arrowAngle));

        pane.getChildren().addAll(line, arrow1, arrow2);
    }

    public double getBlockHeight() {
        return blockHeight;
    }

    public void setBlockHeight( double blockHeight) {
        this.blockHeight = blockHeight;
    }

    @Override protected void dataItemAdded(Series<X,Y> series, int itemIndex, Data<X,Y> item) {
        Node block = createContainer(series, getData().indexOf(series), item, itemIndex);
        getPlotChildren().add(block);
    }

    @Override protected  void dataItemRemoved(final Data<X,Y> item, final Series<X,Y> series) {
        final Node block = item.getNode();
        getPlotChildren().remove(block);
        removeDataItemFromDisplay(series, item);
    }

    @Override protected void dataItemChanged(Data<X, Y> item) {
    }

    @Override protected  void seriesAdded(Series<X,Y> series, int seriesIndex) {
        for (int j=0; j<series.getData().size(); j++) {
            Data<X,Y> item = series.getData().get(j);
            Node container = createContainer(series, seriesIndex, item, j);
            getPlotChildren().add(container);
        }
    }

    @Override protected  void seriesRemoved(final Series<X,Y> series) {
        for (Data<X,Y> d : series.getData()) {
            final Node container = d.getNode();
            getPlotChildren().remove(container);
        }
        removeSeriesFromDisplay(series);

    }


    private Node createContainer(Series<X, Y> series, int seriesIndex, final Data<X,Y> item, int itemIndex) {

        Node container = item.getNode();

        if (container == null) {
            container = new StackPane();
            item.setNode(container);
        }

        //container.getStyleClass().add( getStyleClass( item.getExtraValue()));
        container.setStyle("-fx-background-color: "+getStyleClass(item.getExtraValue()));
        return container;
    }

/*
    @Override protected void updateAxisRange() {
        final Axis<X> xa = getXAxis();
        final Axis<Y> ya = getYAxis();
        List<X> xData = null;
        List<Y> yData = null;
        if(xa.isAutoRanging()) xData = new ArrayList<X>();
        //if(ya.isAutoRanging()) yData = new ArrayList<Y>();
        if(xData != null) {
            for(Series<X,Y> series : getData()) {
                for(Data<X,Y> data: series.getData()) {
                    if(xData != null) {
                        //xData.add(data.getXValue());
                        // x-axis range
                        xData.add(xa.toRealValue((xa.toNumericValue(data.getXValue()) + getlength(data.getExtraValue()))));
                    }
                    if(yData != null){
                        //yData.add(data.getYValue());
                    }
                }
            }
            if(xData != null) xa.invalidateRange(xData);
            if(yData != null) ya.invalidateRange(yData);
        }
    }

   */
}