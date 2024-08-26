EHR.DataEntryUtils.registerGridButton('ADDREQUIREDCLINICALOBS', function(config){
    return Ext4.Object.merge({
        text: 'Add Required Clinical Obs',
        xtype: 'button',
        hidden: true,
        listeners: {
            render: function(btn){
                LABKEY.Query.selectRows({
                    schemaName: 'ehr',
                    queryName: 'observation_types',
                    success: function (results) {
                        var grid = btn.up('gridpanel');
                        if (grid && grid.store && grid.store.data && grid.store.data.getCount() === 0) {
                            if (results && results.rows && results.rows.length > 0) {
                                for (var i = 0; i < results.rows.length; i++) {
                                    var row = results.rows[i];
                                    if (row.value === 'Verified Id?' || row.value === 'Stool' || row.value === 'Activity' ||
                                            row.value === 'Appetite' || row.value === 'Hydration' || row.value === 'BCS') {

                                        var newRecord = grid.store.createModel({});
                                        newRecord.set({
                                            category: row.value,
                                        });
                                        grid.store.add(newRecord);
                                    }
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