
--Amplitude
MAX_AMPLITUDE = 1
MIN_AMPLITUDE = -1

--Angular Frequency
MAX_ANGULAR_FREQUENCY = 1
MIN_ANGULAR_FREQUENCY = -1

--Phase 
MAX_PHASE = math.pi
MIN_PHASE = -math.pi

--Offset
MAX_OFFSET = 1
MIN_OFFSET = -1

function ConvertAnnOutputstoCPGParameters(outputs,faces)

	local amplitude, offset
	local phasediff = {}

	amplitude = ConvertFromUnitToRange(outputs[1],MAX_AMPLITUDE,MIN_AMPLITUDE)
	offset = ConvertFromUnitToRange(outputs[2],MAX_OFFSET,MIN_OFFSET)

	for j=1,faces do
        phasediff[j] = ConvertFromUnitToRange(outputs[j+2],MAX_PHASE,MIN_PHASE)
    end

    return amplitude,offset,phasediff

end


function ConvertFromUnitToRange(value,maxvalue,minvalue)

	local converted = (((value)*(maxvalue-minvalue))) + minvalue

	return converted

end