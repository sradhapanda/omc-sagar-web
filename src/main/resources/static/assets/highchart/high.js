/**
 * 
 */

function column(data,cat,title){
	Highcharts.chart('container', {
	    chart: {
	        type: 'column',
	        marginRight: 120
	    },
	    title: {
	        text: title
	    },
	    subtitle: {
	        text: 'Source: Sample Player report.xlsx'
	    },
	    xAxis: {
	        categories: cat,
	        crosshair: true
	    },
	    legend: {
	    	   layout: 'vertical',
	    	   align: 'right',
	    	   verticalAlign: 'middle',
	    	   itemMarginTop: 10,
	    	   itemMarginBottom: 10
	    	 },
	    yAxis: {
	        min: 0,
	        title: {
	            text: 'Rating'
	        }
	    },
	    tooltip: {
	        headerFormat: '<span style="font-size:14px">{point.key}</span><table>',
	        pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
	            '<td style="padding:0"><b>{point.y:.1f} mm</b></td></tr>',
	        footerFormat: '</table>',
	        shared: true,
	        useHTML: true
	    },
	    plotOptions: {
	        column: {
	            pointPadding: 0.2,
	            borderWidth: 0
	        }
	    },
	    series: data
	
	});
}

function bpiAnalysis(data,cat,div,title){
	Highcharts.chart(div, {
	    chart: {
	        type: 'column',
	        marginRight: 120
	    },
	    title: {
	        text: title
	    },
	    subtitle: {
	        text: 'Source: Sample Player report.xlsx'
	    },
	    xAxis: {
	        categories: cat,
	        crosshair: true
	    },
	    legend: {
	    	   layout: 'vertical',
	    	   align: 'right',
	    	   verticalAlign: 'middle',
	    	   itemMarginTop: 10,
	    	   itemMarginBottom: 10
	    	 },
	    yAxis: {
	        min: 0,
	        title: {
	            text: 'Rating'
	        }
	    },
	    tooltip: {
	        headerFormat: '<span style="font-size:14px">{point.key}</span><table>',
	        pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
	            '<td style="padding:0"><b>{point.y:.1f} </b></td></tr>',
	        footerFormat: '</table>',
	        shared: true,
	        useHTML: true,
	        positioner: function(labelWidth, labelHeight, point) {         
                var tooltipX, tooltipY;
                   if (point.plotX + labelWidth > this.chart.plotWidth) {
                       tooltipX = point.plotX + this.chart.plotLeft - labelWidth - 10;
                   } else {
                       tooltipX = point.plotX + this.chart.plotLeft - 100;
                   }
                   tooltipY = point.plotY + this.chart.plotTop - 200;
                   return {
                       x: tooltipX,
                       y: tooltipY
                   };       
           }
	    },
	    plotOptions: {
	        column: {
	            pointPadding: 0.2,
	            borderWidth: 0
	        },
	        series: {
	            dataLabels: {
	                enabled: true
	            }
	        }
	    },
	    
	    series: data
	
	});
}