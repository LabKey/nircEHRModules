EHR.DataEntryUtils.registerGridButton('ADDGROSSPATHOLOGY', function(config){
    return Ext4.Object.merge({
        text: 'Add',
        xtype: 'button',
        hidden: true,
        listeners: {
            render: function(btn){
                LABKEY.Query.selectRows({
                    schemaName: 'ehr_lookups',
                    queryName: 'necropsy_organ_systems',
                    success: function (results) {
                        var grid = btn.up('gridpanel');
                        if (grid && grid.store && grid.store.data && grid.store.data.getCount() === 0) {
                            if (results && results.rows && results.rows.length > 0) {
                                for (var i = 0; i < results.rows.length; i++) {
                                    var row = results.rows[i];
                                    var newRecord = grid.store.createModel({});
                                    newRecord.set({
                                        systemExamined: row.title,
                                    });
                                    grid.store.add(newRecord);
                                }
                            }
                        }
                    },
                    scope: this
                });
            }
        }
    }, config);
});