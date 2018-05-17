function getTPoints(mseq,Width,initangle)
    local TPoints={}
    local angle = initangle
    local pos = {0,0}
    local nextpos = {0,0}

    for i=1,#mseq,1 do
        TPoints[i] = {}
        if(mseq[i]=='s') then
            
            TPoints[i][1] = pos[1] --Input X
            TPoints[i][2] = pos[2] --Input Y
        
            nextpos[1] = 0*math.cos(angle)-2*math.sin(angle)        
            nextpos[2] = 0*math.sin(angle)+2*math.cos(angle)
            pos[1] = pos[1]+nextpos[1]
            pos[2] = pos[2]+nextpos[2]

            TPoints[i][3] = pos[1] --Output X
            TPoints[i][4] = pos[2] --Output Y
            TPoints[i][5] = angle --Input Angle
            TPoints[i][6] = angle --Output Angle
          
            TPoints[i][7] = 2 --Distance added by the current part
            TPoints[i][8] = 's'

        elseif(mseq[i]=='b') then

        TPoints[i][1] = pos[1] --Input X
        TPoints[i][2] = pos[2] --Input Y    
        
            nextpos[1] = 0*math.cos(angle)-4*math.sin(angle)        
            nextpos[2] = 0*math.sin(angle)+4*math.cos(angle)
            pos[1] = pos[1]+nextpos[1]
            pos[2] = pos[2]+nextpos[2]

        TPoints[i][3] = pos[1] --Output X
        TPoints[i][4] = pos[2] --Output Y
        TPoints[i][5] = angle --Input Angle
        TPoints[i][6] = angle --Output Angle

        TPoints[i][7] = 4 --Distance added by the current part
        TPoints[i][8] = 'b'

        elseif(mseq[i]=='r') then

        TPoints[i][1] = pos[1] --Input X
        TPoints[i][2] = pos[2] --Input Y       
          
    
            xo = (Width/2)+0.075+0.625
            yo = (Width/2) + 0.7
            nextpos[1] = xo*math.cos(angle)-yo*math.sin(angle)        
            nextpos[2] = xo*math.sin(angle)+yo*math.cos(angle)
            pos[1] = pos[1]+nextpos[1]
            pos[2] = pos[2]+nextpos[2]
        
         TPoints[i][3] = pos[1] --Output X
         TPoints[i][4] = pos[2] --Output Y
         TPoints[i][5] = angle --Input Angle

         angle = angle - math.pi/2
         if(angle < -math.pi) then
            angle = math.pi/2
         end
    
         TPoints[i][6] = angle --Output Angle
         --TPoints[i][7] = 2 --Distance added by the current part
         TPoints[i][7] = xo + yo
         TPoints[i][8] = 'r'

        elseif(mseq[i]=='l') then

            
        TPoints[i][1] = pos[1] --Input X
        TPoints[i][2] = pos[2] --Input Y
            
            xo = -(Width/2)-0.075-0.625
            yo = (Width/2) + 0.7
            nextpos[1] = xo*math.cos(angle)-yo*math.sin(angle)        
            nextpos[2] = xo*math.sin(angle)+yo*math.cos(angle)
            pos[1] = pos[1]+nextpos[1]
            pos[2] = pos[2]+nextpos[2]

        TPoints[i][3] = pos[1] --Output X
        TPoints[i][4] = pos[2] --Output Y
        TPoints[i][5] = angle --Input Angle

            angle = angle + math.pi/2 
            if(angle > math.pi) then
                angle = -math.pi/2
            end

        TPoints[i][6] = angle --Output Angle
        --TPoints[i][7] = 2 --Distance added by the current part
        TPoints[i][7] = -xo + yo
        TPoints[i][8] = 'l'
        end
    end
    return TPoints
end

function getDistance(CurrentTPart,TPoints,seqlength,position,initangle,Width,getDistanceToGoal,getDistancebyPartToGoal,distPercent)
    local Goal = false
    local Dxo = 0
    local Dyo = 0
    local sum = 0
    local D = 0
    local Dout = 0

    if(CurrentTPart >= 1) then
               
        if(CurrentTPart<=seqlength) then
	
	-- Check if over output point taking into account output angle
            if(TPoints[CurrentTPart][6] == 0) then
                if(position[2]>TPoints[CurrentTPart][4]) then
                    CurrentTPart = CurrentTPart + 1
                end
            elseif(TPoints[CurrentTPart][6] == math.pi/2) then
                if(position[1]<TPoints[CurrentTPart][3]) then
                    CurrentTPart = CurrentTPart + 1
                end
            elseif(TPoints[CurrentTPart][6] == -math.pi/2) then
                if(position[1]>TPoints[CurrentTPart][3]) then
                    CurrentTPart = CurrentTPart + 1
                end
            elseif(TPoints[CurrentTPart][6] == math.pi or TPoints[CurrentTPart][6] == -math.pi) then
                if(position[2]<TPoints[CurrentTPart][4]) then
                    CurrentTPart = CurrentTPart + 1
                end
            end

	   end

	   if(CurrentTPart<=seqlength) then

            -- Check if over input point taking into account input angle
            if(TPoints[CurrentTPart][5] == 0) then
                if(position[2]<TPoints[CurrentTPart][2]) then
                    CurrentTPart = CurrentTPart - 1
                end
            elseif(TPoints[CurrentTPart][5] == math.pi/2) then
                if(position[1]>TPoints[CurrentTPart][1]) then
                    CurrentTPart = CurrentTPart - 1
                end
            elseif(TPoints[CurrentTPart][5] == -math.pi/2) then
                if(position[1]<TPoints[CurrentTPart][1]) then
                    CurrentTPart = CurrentTPart - 1
                end
            elseif(TPoints[CurrentTPart][5] == math.pi or TPoints[CurrentTPart][5] == -math.pi) then
                if(position[2]>TPoints[CurrentTPart][2]) then
                    CurrentTPart = CurrentTPart - 1
                end
            end
        end

	-- Check if robot got out of the last part
        if(CurrentTPart>seqlength) then
            Goal = true
        end

    else
	
	    if(initangle == 0) then
        	if(position[2]>=0) then
            		CurrentTPart = CurrentTPart + 1
        	end
	    elseif(initangle == math.pi or initangle == -math.pi) then
		    if(position[2]<=0) then
            		CurrentTPart = CurrentTPart + 1
        	end
	    elseif(initangle == math.pi/2) then
		    if(position[1]<=0) then
            		CurrentTPart = CurrentTPart + 1
        	end
	    elseif(initangle == -math.pi/2) then
		    if(position[1]>=0) then
            		CurrentTPart = CurrentTPart + 1
        	end
    	end

    end	

    --goalX = TPoints[seqlength][3]
    --goalY = TPoints[seqlength][4]
        
    if(CurrentTPart>=1) then

        --print(CurrentTPart)

        goalX,goalY,outAngle = getGoalPosition(TPoints[CurrentTPart],distPercent)
        --print(goalX,goalY,outAngle)

        Goal = isOverGoal(goalX,goalY,outAngle,position)

        if(CurrentTPart<=seqlength) then
            if getDistanceToGoal then
                if getDistancebyPartToGoal then
                    --Get Manhattan distance based on part
                    Do = GetPartManhattanDistance(TPoints,CurrentTPart,position)
                    sum = 0
                    for i=CurrentTPart+1,seqlength,1 do
                        sum = sum + TPoints[i][7]
                    end
                    D = Do + sum
                else
                    --Get Manhattan distance based on goal 
                    Dxo = math.abs(goalX-position[1])
                    Dyo = math.abs(goalY-position[2])
                    D = Dxo + Dyo
                end
            else
                Dxi = math.abs(0-position[1])
                Dyi = math.abs(0-position[2])
                D = Dxi + Dyi
            end
            --print(Do,sum,D)
            --print(D)
        end
    else

        if getDistanceToGoal then
            Dxo =  math.abs(0-position[1])
            Dyo =  math.abs(0-position[2])
            Do = Dxo + Dyo

            if getDistancebyPartToGoal then
                --Get Manhattan distance based on part
                sum = 0
                for i=1,seqlength,1 do
                    sum = sum + TPoints[i][7]
                end
            else
                --Get Manhattan distance based on goal
                Dxt = math.abs(goalX-0)
                Dyt = math.abs(goalY-0)
                sum = Dxt + Dyt
            end
            D = Do + sum
        else
            D = 0
        end
        --print(D)
    end
    -- Normalize Distance
    if getDistanceToGoal then
        if getDistancebyPartToGoal then
            --Get Manhattan distance based on part
            sum = 0
            for i=1,seqlength,1 do
                sum = sum + TPoints[i][7]
            end
        else
            --Get Manhattan distance based on goal
            Dxt = math.abs(goalX-0)
            Dyt = math.abs(goalY-0)
            sum = Dxt + Dyt
        end
    else
        Dxt = math.abs(goalX-0)
        Dyt = math.abs(goalY-0)
        sum = Dxt + Dyt + Width/2

    end

    Dout = D/sum

    if(Dout>1) then
        Dout = 1
    end

    if Goal then
        if getDistanceToGoal then
            Dout = 0
        else
            Dout = 1
        end
    end

    print(getDistanceToGoal,Goal)
    print(Dout)
    return CurrentTPart,Goal,Dout
end


function getTPointsS(mseq,Width,initangle)
    local TPoints={}
    local angle = initangle
    local pos = {0,0}
    local nextpos = {0,0}

    for i=1,#mseq,1 do
        TPoints[i] = {}
        if(mseq[i]=='s') then
            
            TPoints[i][1] = pos[1] --Input X
            TPoints[i][2] = pos[2] --Input Y
        
            nextpos[1] = 0*math.cos(angle)-1*math.sin(angle)        
            nextpos[2] = 0*math.sin(angle)+1*math.cos(angle)
            pos[1] = pos[1]+nextpos[1]
            pos[2] = pos[2]+nextpos[2]

            TPoints[i][3] = pos[1] --Output X
            TPoints[i][4] = pos[2] --Output Y
            TPoints[i][5] = angle --Input Angle
            TPoints[i][6] = angle --Output Angle
          
            TPoints[i][7] = 1 --Distance added by the current part
            TPoints[i][8] = 's'

        elseif(mseq[i]=='b') then

        TPoints[i][1] = pos[1] --Input X
        TPoints[i][2] = pos[2] --Input Y    
        
            nextpos[1] = 0*math.cos(angle)-0.5*math.sin(angle)        
            nextpos[2] = 0*math.sin(angle)+0.5*math.cos(angle)
            pos[1] = pos[1]+nextpos[1]
            pos[2] = pos[2]+nextpos[2]

        TPoints[i][3] = pos[1] --Output X
        TPoints[i][4] = pos[2] --Output Y
        TPoints[i][5] = angle --Input Angle
        TPoints[i][6] = angle --Output Angle

        TPoints[i][7] = 0.5 --Distance added by the current part
        TPoints[i][8] = 'b'

        elseif(mseq[i]=='r') then

        TPoints[i][1] = pos[1] --Input X
        TPoints[i][2] = pos[2] --Input Y       
          
    
            xo = (Width/2)+0.075+0.125
            yo = (Width/2) + 0.2
            nextpos[1] = xo*math.cos(angle)-yo*math.sin(angle)        
            nextpos[2] = xo*math.sin(angle)+yo*math.cos(angle)
            pos[1] = pos[1]+nextpos[1]
            pos[2] = pos[2]+nextpos[2]
        
         TPoints[i][3] = pos[1] --Output X
         TPoints[i][4] = pos[2] --Output Y
         TPoints[i][5] = angle --Input Angle

         angle = angle - math.pi/2
         if(angle < -math.pi) then
            angle = math.pi/2
         end
    
         TPoints[i][6] = angle --Output Angle
         --TPoints[i][7] = 2 --Distance added by the current part
         --TPoints[i][7] = xo + yo
         TPoints[i][7] = xo + yo + (Width/2) --Distance added in Manhattan distance
         TPoints[i][8] = 'r'

        elseif(mseq[i]=='l') then

            
        TPoints[i][1] = pos[1] --Input X
        TPoints[i][2] = pos[2] --Input Y
            
            xo = -(Width/2)-0.075-0.125
            yo = (Width/2) + 0.2
            nextpos[1] = xo*math.cos(angle)-yo*math.sin(angle)        
            nextpos[2] = xo*math.sin(angle)+yo*math.cos(angle)
            pos[1] = pos[1]+nextpos[1]
            pos[2] = pos[2]+nextpos[2]

        TPoints[i][3] = pos[1] --Output X
        TPoints[i][4] = pos[2] --Output Y
        TPoints[i][5] = angle --Input Angle

            angle = angle + math.pi/2 
            if(angle > math.pi) then
                angle = -math.pi/2
            end

        TPoints[i][6] = angle --Output Angle
        --TPoints[i][7] = 2 --Distance added by the current part
        --TPoints[i][7] = -xo + yo
         TPoints[i][7] = -xo + yo + (Width/2) --Distance added in Manhattan distance
        TPoints[i][8] = 'l'
        end
    end
    return TPoints
end


function GetPartManhattanDistance(TPoints,CurrentPart,position)

    if (TPoints[CurrentPart][8]=='s' or TPoints[CurrentPart][8] == 'b') then
        
            Dyo = math.abs(TPoints[CurrentPart][4]-position[2])
            Dxo = math.abs(TPoints[CurrentPart][3]-position[1])
            Do = Dxo+Dyo; 
            return Do       

    elseif (TPoints[CurrentPart][8]=='r' or TPoints[CurrentPart][8]=='l') then
        --Take into account output angle
        angle = TPoints[CurrentPart][6]
        outputX = TPoints[CurrentPart][3]
        outputY = TPoints[CurrentPart][4]
        inputX = TPoints[CurrentPart][1]
        inputY = TPoints[CurrentPart][2]
        --print(angle,outputX,outputY,inputX,inputY)
        if(angle == 0 or angle == math.pi or angle == -math.pi) then
            DyoinputLine = math.abs(inputY-position[2])
            DxoouputLine = math.abs(outputX-position[1])
            --print(angle,DyoinputLine,DxoouputLine)
            if (DyoinputLine<DxoouputLine) then
                outputLine = math.abs(outputY-inputY)
                rInputLine = math.abs(outputX-position[1])
                Do = outputLine + rInputLine + DyoinputLine
                --print(outputLine,rInputLine,DyoinputLine,Do)
            else
                routputLine = math.abs(outputY-position[2])
                Do = routputLine + DxoouputLine
                --print(routputLine,DxoouputLine,Do)
            end
            return Do
        elseif(angle == math.pi/2 or angle == -math.pi/2) then
            Dxoinputline = math.abs(inputX-position[1])
            Dyooutputline = math.abs(outputY-position[2])
            --print(angle,Dxoinputline,Dyooutputline)
            if (Dxoinputline<Dyooutputline) then
                outputLine = math.abs(outputX-inputX)
                rInputLine = math.abs(outputY-position[2])
                Do = outputLine + rInputLine + Dxoinputline 
            else
                routputLine = math.abs(outputX-position[1])
                Do = routputLine + Dyooutputline
            end
            return Do  
        end  
    end
    
end

function getGoalPosition(TPart,distPercent)

    if(TPart[8]=='s') then
        angle = TPart[6]
        if (angle==0 or angle == math.pi or angle == -math.pi) then
            goalX = TPart[3]
            goalY = distPercent*TPart[4]
        elseif(angle == math.pi/2 or angle == -math.pi/2) then
            goalX = distPercent*TPart[3]
            goalY = TPart[4]
        end
    end
    outAngle = angle
    return goalX,goalY,outAngle
end

function isOverGoal(goalX,goalY,outAngle,position)

    if(outAngle == 0) then
        if(position[2]>goalY) then
            return true
        end
    elseif(outAngle == math.pi/2) then
        if(position[1]<goalX) then
            return true
        end
    elseif(outAngle == -math.pi/2) then
        if(position[1]>goalX) then
            return true
        end
    elseif(outAngle == math.pi or outAngle == -math.pi) then
        if(position[2]<goalY) then
            return true
        end
    end

    return false

end


