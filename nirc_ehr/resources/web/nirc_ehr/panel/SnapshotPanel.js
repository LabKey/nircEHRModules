Ext4.define('NIRC_EHR.panel.SnapshotPanel', {
    extend: 'EHR.panel.SnapshotPanel',
    alias: 'widget.nirc_ehr-snapshotpanel',

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
        toSet['assignments'] = null;

        if (this.redacted) {
            return;
        }

        var values = [];
        if (results){
            Ext4.each(results, function(row){
                var val = row['protocolTitle'] || '';
                val += ' - ' + (row['investigatorLastName'] || row['investigatorId'] || row['investigatorName']);
                val += ' - ' + (row['project']);

                if (val)
                    values.push(LABKEY.Utils.encodeHtml(val));
            }, this);
        }

        toSet['assignments'] = values.length ? values.join('<br>') : 'None';
    },

});