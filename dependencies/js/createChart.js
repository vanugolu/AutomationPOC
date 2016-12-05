function createChart(data) {
        $("#chart").kendoChart({
        	seriesColors: ["green", "red"],
            theme: $(document).data("kendoSkin") || "default",
            title: {
                text: "Automation History"
            },
            legend: {
                position: "bottom"
            },
            dataSource: {
                data: data
            },
            series: [{
                type: "pie",
                field: "percentage",
                categoryField: "source",
                explodeField: "explode"
            }],
            tooltip: {
                visible: true,
                template: "${ category } - ${ value }%"
            },
            seriesClick: function(e){
              $( e.sender.dataSource.options.data ).each( function ( i, item )
              {
                if ( item.source != e.category )
                {
                    item.explode = false;
                }
                else
                {
                    item.explode = true;
                }
              } );
              createChart(data);
            }
        });
    }
    
    function getCount(){
    	//var mytable = document.getElementById('myTable').childNodes[3].childNodes[2].getElementsByTagName("td");
        var mytable = $('#myTable .myStyle2 td');
        //myTable = mytable.find("b").text().split('  ');
    	//var Cells = mytable.getElementsByTagName("b");
    	//for(var i=0;i<mytable.length;i++)
    	//document.write(Cells[i].innerHTML);
    	var total = $(mytable[6]).text().replace(",","");
    	var pass = $(mytable[7]).text().replace(",","");
    	var fail = $(mytable[8]).text().replace(",","");
        pass = parseInt((pass/total) * 100);
        fail = 100 - pass;
        var data = [
                    {
                        "source": "Pass",
                        "percentage": pass,
                        "explode": true
                    },
                    {
                        "source": "Fail",
                        "percentage": fail
                    },                    
        ];
        //return data;
        
        setTimeout(function() {
            // Initialize the chart with a delay to make sure
            // the initial animation is visible
            createChart(data);

            $("#example").bind("kendo:skinChange", function(e) {
                createChart(data);
            });
        }, 400);
    	
    }
