Ext4.define('NIRC_EHR.panel.SnapshotPanel', {
    extend: 'EHR.panel.SnapshotPanel',
    alias: 'widget.nirc_ehr-snapshotpanel',

    getBaseItems: function(){
        return [{
            xtype: 'container',
            border: false,
            defaults: {
                border: false
            },
            items: [{
                xtype: 'container',
                html: '<b>Summary:</b><hr>'
            },{
                bodyStyle: 'padding-bottom: 20px;',
                layout: 'column',
                defaults: {
                    border: false
                },
                items: [{
                    xtype: 'container',
                    columnWidth: 0.25,
                    defaults: {
                        labelWidth: this.defaultLabelWidth,
                        style: 'margin-right: 20px;'
                    },
                    items: [{
                        xtype: 'displayfield',
                        fieldLabel: 'Location',
                        name: 'location'
                    },{
                        xtype: 'displayfield',
                        hidden: this.redacted,
                        name: 'protocolAssignment',
                        fieldLabel: 'Protocol'
                    },{
                        xtype: 'displayfield',
                        hidden: this.redacted,
                        name: 'projectAssignment',
                        fieldLabel: 'Project'
                    },{
                        xtype: 'displayfield',
                        fieldLabel: 'Source',
                        name: 'source'
                    },{
                        xtype: 'displayfield',
                        fieldLabel: 'Prev Id',
                        name: 'prev_id'
                    }]
                },{
                    xtype: 'container',
                    columnWidth: 0.25,
                    defaults: {
                        labelWidth: this.defaultLabelWidth,
                        style: 'margin-right: 20px;'
                    },
                    items: [{
                        xtype: 'displayfield',
                        fieldLabel: 'Status',
                        name: 'calculated_status'
                    },{
                        xtype: 'displayfield',
                        fieldLabel: 'Sex',
                        name: 'gender'
                    },{
                        xtype: 'displayfield',
                        fieldLabel: 'Species',
                        name: 'species'
                    },{
                        xtype: 'displayfield',
                        fieldLabel: 'Age',
                        name: 'age'
                    }]
                },{
                    xtype: 'container',
                    columnWidth: 0.35,
                    defaults: {
                        labelWidth: this.defaultLabelWidth,
                        style: 'margin-right: 20px;'
                    },
                    items: [{
                        xtype: 'displayfield',
                        fieldLabel: 'Flags',
                        name: 'flags'
                    },{
                        xtype: 'displayfield',
                        fieldLabel: 'Remark',
                        name: 'remark'
                    },{
                        xtype: 'displayfield',
                        fieldLabel: 'Last TB',
                        name: 'lastTB'
                    },{
                        xtype: 'displayfield',
                        fieldLabel: 'Weights',
                        name: 'weights'
                    }]
                }]
            }]
        }];
    },

    appendDemographicsResults: function(toSet, row, id){
        if (!row){
            console.log('Id not found');
            return;
        }

        var animalId = row.getId() || id;
        if (!Ext4.isEmpty(animalId)){
            toSet['animalId'] = LABKEY.Utils.encodeHtml(id);
        }

        var status = row.getCalculatedStatus() || 'Unknown';
        toSet['calculated_status'] = '<span ' + (status.toLowerCase() !== 'alive' ? 'style="background-color:yellow"' : '') + '>'
                + LABKEY.Utils.encodeHtml(status) + '</span>';

        toSet['species'] = LABKEY.Utils.encodeHtml(row.getSpeciesCommonName());
        toSet['geographic_origin'] = LABKEY.Utils.encodeHtml(row.getGeographicOrigin());
        toSet['gender'] = LABKEY.Utils.encodeHtml(row.getGender());
        toSet['age'] = LABKEY.Utils.encodeHtml(row.getAgeInYearsAndDays());

        var location;
        if (row.getActiveHousing() && row.getActiveHousing().length){
            var housingRow = row.getActiveHousing();
            location = '';
            if (housingRow)
                location +=  LABKEY.Utils.encodeHtml(housingRow[0]?.['cage/cage']);

            if (location){
                if (this.showLocationDuration && housingRow[0].date){
                    var date = LDK.ConvertUtils.parseDate(housingRow[0].date);
                    if (date)
                        location += ' (' + Ext4.Date.format(date, LABKEY.extDefaultDateFormat) + ')';
                }
            }
        }

        toSet['location'] = location || 'No active housing';
        this.appendProtocolAssignment(toSet, row);
    },

    appendFlags: function(toSet, results){
        var values = [];
        if (results){
            Ext4.each(results, function(row){
                var category = row['flag/category'];
                var highlight = row['flag/category/doHighlight'];
                var omit = row['flag/category/omitFromOverview'];

                //skip
                if (omit === true)
                    return;

                if (category)
                    category = Ext4.String.trim(category);

                var val = LABKEY.Utils.encodeHtml(this.getFlagDisplayValue(row));
                var text = val;
                if (category)
                    text = LABKEY.Utils.encodeHtml(category) + ': ' + val;

                if (text && highlight)
                    text = '<span style="background-color:yellow">' + text + '</span>';

                if (text)
                    values.push(text);
            }, this);

            if (values.length) {
                values = Ext4.unique(values);
            }
        }

        toSet['flags'] = values.length ? '<a onclick="NIRC_EHR.Utils.showFlagPopup(\'' + LABKEY.Utils.encodeHtml(this.subjectId) + '\', this);">' + values.join('<br>') + '</div>' : null;
    },

    appendAssignments: function(toSet, results){
        toSet['projectAssignment'] = null;

        if (this.redacted) {
            return;
        }

        var values = [];
        if (results){
            Ext4.each(results, function(row){
                if (row['project']) {
                    values.push(LABKEY.Utils.encodeHtml(row['project']));
                }
            }, this);
        }

        toSet['projectAssignment'] = values.length ? values.join('<br>') : 'None';
    },

    appendProtocolAssignment: function(toSet, results){
        let paRecords = results.getData()['protocolAssignments'];
        let values = [];

        let val;
        if (Ext4.isArray(paRecords) && paRecords.length > 0) {
            Ext4.each(paRecords, function(record) {
                val = record['protocol/displayName'];
                if (record['protocol/InvestigatorId/lastName']) {
                    val += " - " + LABKEY.Utils.encodeHtml(record['protocol/InvestigatorId/lastName']);
                }
                if (record['protocol/InvestigatorId/firstName']) {
                    val += ", " + LABKEY.Utils.encodeHtml(record['protocol/InvestigatorId/firstName']);
                }
                values.push(LABKEY.Utils.encodeHtml(val));
            });
        }

        toSet['protocolAssignment'] = values.length > 0 ? values.join('<br/>') : 'None';
    },
});